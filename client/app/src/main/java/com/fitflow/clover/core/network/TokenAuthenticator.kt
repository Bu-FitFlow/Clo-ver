package com.fitflow.clover.core.network

import android.util.Log
import com.fitflow.clover.data.local.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenAuthenticator(
    private val tokenDataStore: TokenDataStore
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        if (responseCount(response) >= 2) {
            Log.e(TAG, "토큰 재발급 중단: 재시도 횟수 초과")
            return null
        }

        val hasAuthorizationHeader =
            response.request.header(NetworkConstants.HEADER_AUTHORIZATION) != null

        if (!hasAuthorizationHeader) {
            Log.e(TAG, "토큰 재발급 중단: 기존 요청에 Authorization 헤더가 없음")
            return null
        }

        val refreshToken = runBlocking {
            tokenDataStore.getRefreshToken()
        }

        if (refreshToken.isNullOrBlank()) {
            Log.e(TAG, "토큰 재발급 중단: refreshToken 없음")

            runBlocking {
                tokenDataStore.clearTokens()
            }

            return null
        }

        val requestBody = JSONObject()
            .put("refreshToken", refreshToken)
            .toString()
            .toRequestBody("application/json".toMediaType())

        val refreshRequest = Request.Builder()
            .url("${NetworkConstants.BASE_URL}api/members/refresh")
            .post(requestBody)
            .header("Authorization-Refresh", refreshToken)
            .header(NetworkConstants.HEADER_CONTENT_TYPE, "application/json")
            .build()

        return try {
            OkHttpClient().newCall(refreshRequest).execute().use { refreshResponse ->
                val responseBody = refreshResponse.body?.string().orEmpty()

                if (!refreshResponse.isSuccessful) {
                    Log.e(
                        TAG,
                        "토큰 재발급 실패: HTTP ${refreshResponse.code}, body=$responseBody"
                    )

                    runBlocking {
                        tokenDataStore.clearTokens()
                    }

                    return null
                }

                val tokenPair = responseBody.extractTokenPair()
                val newAccessToken = tokenPair.accessToken
                val newRefreshToken = tokenPair.refreshToken
                    ?.takeIf { it.isNotBlank() }
                    ?: refreshToken

                if (newAccessToken.isNullOrBlank()) {
                    Log.e(TAG, "토큰 재발급 실패: accessToken 없음, body=$responseBody")

                    runBlocking {
                        tokenDataStore.clearTokens()
                    }

                    return null
                }

                runBlocking {
                    tokenDataStore.saveTokens(
                        accessToken = newAccessToken,
                        refreshToken = newRefreshToken
                    )
                }

                Log.d(TAG, "토큰 재발급 성공")

                response.request.newBuilder()
                    .header(
                        NetworkConstants.HEADER_AUTHORIZATION,
                        newAccessToken.toBearerHeaderValue()
                    )
                    .build()
            }
        } catch (e: Exception) {
            Log.e(TAG, "토큰 재발급 통신 실패: ${e.message}", e)

            runBlocking {
                tokenDataStore.clearTokens()
            }

            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse

        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }

        return count
    }

    private fun String.extractTokenPair(): TokenPair {
        if (isBlank()) {
            return TokenPair()
        }

        return runCatching {
            val root = JSONObject(this)

            val tokenObject = root.objectOrNull("data")
                ?: root.objectOrNull("payload")
                ?: root.objectOrNull("result")
                ?: root.objectOrNull("body")
                ?: root.objectOrNull("token")
                ?: root.objectOrNull("tokens")
                ?: root

            TokenPair(
                accessToken = tokenObject.stringOrNull(
                    "accessToken",
                    "access_token",
                    "token"
                ) ?: root.stringOrNull(
                    "accessToken",
                    "access_token",
                    "token"
                ),
                refreshToken = tokenObject.stringOrNull(
                    "refreshToken",
                    "refresh_token"
                ) ?: root.stringOrNull(
                    "refreshToken",
                    "refresh_token"
                )
            )
        }.getOrElse { error ->
            Log.e(TAG, "토큰 응답 파싱 실패: ${error.message}", error)
            TokenPair()
        }
    }

    private fun JSONObject.objectOrNull(key: String): JSONObject? {
        return optJSONObject(key)
    }

    private fun JSONObject.stringOrNull(vararg keys: String): String? {
        for (key in keys) {
            val value = optString(key, null)

            if (!value.isNullOrBlank() && value != "null") {
                return value
            }
        }

        return null
    }

    private fun String.toBearerHeaderValue(): String {
        val token = trim()

        return if (token.startsWith("${NetworkConstants.BEARER_PREFIX} ", ignoreCase = true)) {
            token
        } else {
            "${NetworkConstants.BEARER_PREFIX} $token"
        }
    }

    private data class TokenPair(
        val accessToken: String? = null,
        val refreshToken: String? = null
    )

    companion object {
        private const val TAG = "TokenAuthenticator"
    }
}