package com.fitflow.clover.core.network

import android.content.Context
import android.util.Log
import com.fitflow.clover.data.remote.dto.LoginRequest
import com.fitflow.clover.data.remote.dto.toDomain

class BackendConnectionTester(
    private val context: Context
) {
    suspend fun run(
        loginId: String,
        password: String
    ) {
        val networkModule = NetworkModule(context)

        try {
            val healthResult = networkModule.healthApi.checkHealth().string()

            Log.d(TAG, "1. HealthApi 호출 성공: $healthResult")

            val loginResponse = networkModule.authApi.login(
                LoginRequest(
                    loginId = loginId,
                    password = password
                )
            )

            val accessToken = loginResponse.accessToken
            val refreshToken = loginResponse.refreshToken

            if (accessToken.isNullOrBlank()) {
                Log.e(TAG, "2. Login 실패: accessToken이 비어 있음")
                return
            }

            networkModule.tokenDataStore.saveTokens(
                accessToken = accessToken,
                refreshToken = refreshToken
            )

            val savedAccessToken = networkModule.tokenDataStore.getAccessToken()

            Log.d(
                TAG,
                "2. Login 성공 및 accessToken 저장 성공: ${savedAccessToken?.take(20)}..."
            )

            val chatRoomResponses = networkModule.chatApi.getChatRooms()
            val chatRoomDomains = chatRoomResponses.map { it.toDomain() }

            Log.d(
                TAG,
                "3-4. Chat API 호출 및 Domain 변환 성공: DTO=${chatRoomResponses.size}, Domain=${chatRoomDomains.size}"
            )

            val communityResponses = networkModule.communityApi.getPosts(
                category = null,
                page = 0,
                size = 5
            )
            val communityDomains = communityResponses.map { it.toDomain() }

            Log.d(
                TAG,
                "3-4. Community API 호출 및 Domain 변환 성공: DTO=${communityResponses.size}, Domain=${communityDomains.size}"
            )

            Log.d(TAG, "전체 백엔드 연결 테스트 성공")

        } catch (e: Exception) {
            Log.e(TAG, "백엔드 연결 테스트 실패", e)
        }
    }

    companion object {
        private const val TAG = "CloverBackendTest"
    }
}