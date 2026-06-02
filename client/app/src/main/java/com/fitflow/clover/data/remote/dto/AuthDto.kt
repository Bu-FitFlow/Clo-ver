package com.fitflow.clover.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 📤 회원가입 요청 바디 (명세서 규격 1:1 매칭)
@Serializable
data class SignUpRequest(
    @SerialName("loginId") val loginId: String,
    @SerialName("password") val password: String,
    @SerialName("name") val name: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("email") val email: String,
    @SerialName("gender") val gender: String // "MALE" 또는 "FEMALE"
)

// 📥 회원가입 성공 응답 바디 (명세서 규격 1:1 매칭)
@Serializable
data class SignUpResponse(
    @SerialName("memberId") val memberId: Int,
    @SerialName("loginId") val loginId: String,
    @SerialName("name") val name: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("email") val email: String,
    @SerialName("gender") val gender: String,
    @SerialName("role") val role: String
)