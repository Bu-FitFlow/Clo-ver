package com.fitflow.clover.core.network

import com.fitflow.clover.data.local.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenDataStore: TokenDataStore
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        val hasToken = response.request.header(NetworkConstants.HEADER_AUTHORIZATION) != null
        if (!hasToken) {
            return null
        }

        runBlocking {
            tokenDataStore.clearTokens()
        }

        return null
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
