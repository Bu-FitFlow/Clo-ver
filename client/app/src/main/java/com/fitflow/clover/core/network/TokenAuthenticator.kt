package com.fitflow.clover.core.network

import android.util.Log
import com.fitflow.clover.data.local.TokenDataStore
import com.fitflow.clover.data.remote.api.AuthApi
import com.fitflow.clover.data.remote.dto.TokenRefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenDataStore: TokenDataStore,
    // 💡 핵심: 무한 루프(순환 참조)를 막기 위해 AuthApi를 직접 받지 않고 '필요할 때만 꺼내 쓰는 람다'로 받습니다!
    private val authApiProvider: () -> AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val hasToken = response.request.header("Authorization") != null
        if (!hasToken) return null

        // 금고에서 리프레시 토큰 꺼내기
        val refreshToken = runBlocking { tokenDataStore.getRefreshToken() }

        if (refreshToken.isNullOrEmpty()) {
            runBlocking { tokenDataStore.clearTokens() }
            return null
        }

        return runBlocking {
            try {
                // 🚀 날것의 OkHttp 대신, 우리가 만든 AuthApi를 호출합니다!
                // (이전에 작성하신 AuthApi.kt의 refreshToken 함수를 그대로 활용)
                val refreshResponse = authApiProvider().refreshToken(
                    TokenRefreshRequest(refreshToken = refreshToken)
                )

                val newAccessToken = refreshResponse.accessToken
                val newRefreshToken = refreshResponse.refreshToken

                if (!newAccessToken.isNullOrBlank()) {
                    // 새 토큰 저장
                    tokenDataStore.saveTokens(newAccessToken, newRefreshToken)
                    Log.d("TokenAuthenticator", "토큰 갱신 성공!")

                    // 🚀 실패했던 원래 통신에 새 엑세스 토큰 달아서 심폐소생술!
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    tokenDataStore.clearTokens()
                    return@runBlocking null
                }
            } catch (e: Exception) {
                Log.e("TokenAuthenticator", "토큰 갱신 실패", e)
                tokenDataStore.clearTokens()
                return@runBlocking null
            }
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
}