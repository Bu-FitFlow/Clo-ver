package com.fitflow.clover.core.network

import com.fitflow.clover.core.data.model.SignupRequest
import com.fitflow.clover.core.data.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
interface AuthService {
    // 수형님의 회원가입 API 주소 매핑
    @POST("/api/members/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<SignupResponse>
}
object NetworkModule {
    // 💡 만약 조원들이 이미 만들어 둔 baseUrl이나 retrofit 객체가 있다면 그 명칭을 그대로 쓰셔야 합니다!
    private const val BASE_URL = "https://clo-ver.shop"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 🎯 우리가 만든 수형님 서버 전송용 리모컨을 여기에 등록합니다.
    val authService: AuthService = retrofit.create(AuthService::class.java)
}