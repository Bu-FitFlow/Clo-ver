package com.fitflow.clover.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitflow.clover.data.model.SignupRequest
import com.fitflow.clover.data.model.SignupResponse
import com.fitflow.clover.core.network.NetworkModule
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    // 🎯 [수정] 가짜 값 대신, 실제 AuthScreen(UI)에서 사용자가 입력한 값을 매개변수로 받도록 수정했습니다!
    fun registerRequest(
        loginIdInput: String,
        passwordInput: String,
        nameInput: String,
        nicknameInput: String,
        emailInput: String,
        genderInput: String
    ) {
        // 1. 사용자가 화면에 입력한 진짜 데이터들을 가방에 예쁘게 담습니다.
        val studentData = SignupRequest(
            loginId = loginIdInput,
            password = passwordInput,
            name = nameInput,
            nickname = nicknameInput,
            email = emailInput,
            gender = genderInput
        )

        // 2. 코루틴을 열어서 백그라운드에서 수형님 서버와 통신 시작!
        viewModelScope.launch {
            try {
                // 🎯 [에러 해결] studentData 가방을 수형님 서버 통로로 안전하게 던집니다.
                val response = NetworkModule.authService.signup(studentData)

                if (response.isSuccessful && response.body() != null) {
                    // 3. 수형님이 새로 설계한 영수증(SignupResponse) 데이터 수신 완료
                    val signupResult: SignupResponse = response.body()!!

                    // 테스트용 로그 확인
                    println("회원가입이 완료되었습니다: ${signupResult.nickname}님 환영합니다!")

                    // TODO: 조원들이 만든 가입 성공 후 로직 추가 (예: 로그인 화면으로 백)
                } else {
                    // 서버가 거절했을 때 (아이디 중복, 비밀번호 규칙 위반 등)
                    println("회원가입 거절됨 (에러 코드): ${response.code()}")
                }
            } catch (e: Exception) {
                // 인터넷 연결 끊김 등 통신 자체 실패 오류 처리
                e.printStackTrace()
            }
        }
    }
}