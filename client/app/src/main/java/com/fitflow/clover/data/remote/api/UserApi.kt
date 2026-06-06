package com.fitflow.clover.data.remote.api

import com.fitflow.clover.data.remote.dto.MemberUpdateRequest
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("api/members/me")
    suspend fun getMyInfo(): JsonObject

    @PATCH("api/members/me")
    suspend fun updateMyInfo(
        @Body request: MemberUpdateRequest
    ): JsonObject
}
