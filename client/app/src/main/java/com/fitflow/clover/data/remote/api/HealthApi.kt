package com.fitflow.clover.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET

interface HealthApi {
    @GET("api/health")
    suspend fun checkHealth(): ResponseBody
}
