package com.fitflow.clover.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenDataStore(
    context: Context
) {
    private val preferences = context.applicationContext.getSharedPreferences(
        TOKEN_STORE_NAME,
        Context.MODE_PRIVATE
    )

    private val _accessTokenFlow = MutableStateFlow(
        preferences.getString(ACCESS_TOKEN_KEY, null)
    )
    val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    private val _refreshTokenFlow = MutableStateFlow(
        preferences.getString(REFRESH_TOKEN_KEY, null)
    )
    val refreshTokenFlow: StateFlow<String?> = _refreshTokenFlow.asStateFlow()

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String? = null
    ) {
        preferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .apply()
        _accessTokenFlow.value = accessToken

        if (!refreshToken.isNullOrBlank()) {
            preferences.edit()
                .putString(REFRESH_TOKEN_KEY, refreshToken)
                .apply()
            _refreshTokenFlow.value = refreshToken
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        preferences.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .apply()
        _accessTokenFlow.value = accessToken
    }

    suspend fun getAccessToken(): String? {
        return preferences.getString(ACCESS_TOKEN_KEY, null)
    }

    suspend fun getRefreshToken(): String? {
        return preferences.getString(REFRESH_TOKEN_KEY, null)
    }

    suspend fun clearTokens() {
        preferences.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()
        _accessTokenFlow.value = null
        _refreshTokenFlow.value = null
    }

    companion object {
        private const val TOKEN_STORE_NAME = "clover_token_store"
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}
