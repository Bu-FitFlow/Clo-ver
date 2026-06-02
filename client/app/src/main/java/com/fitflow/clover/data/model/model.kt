package com.fitflow.clover.data.model

// 1. [요청] 내가 서버로 보낼 회원가입 데이터 가방 (기존 동일)
data class SignupRequest(
    val loginId: String,
    val password: String,
    val name: String,
    val nickname: String,
    val email: String,
    val gender: String // "MALE" 혹은 "FEMALE"
)

// 2. [응답 - 병합 완료] 수형님 서버가 회원가입 성공(200 OK) 시 돌려주는 데이터 구조
data class SignupResponse(
    val memberId: Int,       // 0으로 적혀있는 숫자는 Int로 매핑합니다.
    val loginId: String,
    val name: String,
    val nickname: String,
    val email: String,
    val gender: String,      // "MALE"
    val role: String         // "string" (유저 권한 유형)
)