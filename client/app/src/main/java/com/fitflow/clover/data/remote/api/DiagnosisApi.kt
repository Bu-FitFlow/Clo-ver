package com.fitflow.clover.data.remote.api

import com.fitflow.clover.data.remote.dto.DiagnosisApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DiagnosisApi {

    @Multipart
    @POST("api/diagnoses/body")
    suspend fun createBodyScan(
        @Part("memberId") memberId: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("height") height: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part frontImg: MultipartBody.Part,
        @Part sideImg: MultipartBody.Part
    ): DiagnosisApiResponse

    @Multipart
    @POST("api/diagnoses/color")
    suspend fun createPersonalColorScan(
        @Part("memberId") memberId: RequestBody,
        @Part frontImg: MultipartBody.Part
    ): DiagnosisApiResponse
}