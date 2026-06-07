package com.fitflow.clover.core.network

import android.util.Log
import com.fitflow.clover.data.local.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        if (shouldSkipAuth(path)) {
            Log.d(TAG, "Authorization 생략: path=$path")
            return chain.proceed(originalRequest)
        }

        if (originalRequest.header(NetworkConstants.HEADER_AUTHORIZATION) != null) {
            Log.d(TAG, "Authorization 이미 존재: path=$path")
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking {
            tokenDataStore.getAccessToken()
        }

        if (accessToken.isNullOrBlank()) {
            Log.e(TAG, "Authorization 추가 실패: accessToken 없음, path=$path")
            return chain.proceed(originalRequest)
        }

        val request = originalRequest.newBuilder()
            .header(
                NetworkConstants.HEADER_AUTHORIZATION,
                accessToken.toBearerHeaderValue()
            )
            .build()

        Log.d(TAG, "Authorization 추가 완료: path=$path")

        return chain.proceed(request)
    }

    private fun shouldSkipAuth(path: String): Boolean {
        return path == "/api/health" ||
                path.contains("/login") ||
                path.contains("/signup") ||
                path.contains("/sign-up") ||
                path.contains("/refresh") ||
                path.contains("/reissue")
    }

    private fun String.toBearerHeaderValue(): String {
        val token = trim()

        return if (token.startsWith("${NetworkConstants.BEARER_PREFIX} ", ignoreCase = true)) {
            token
        } else {
            "${NetworkConstants.BEARER_PREFIX} $token"
        }
    }

    companion object {
        private const val TAG = "AuthInterceptor"
    }
}