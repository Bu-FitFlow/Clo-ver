package com.fitflow.clover.presentation.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitflow.clover.core.network.NetworkModule
import com.fitflow.clover.data.remote.dto.LoginRequest
import kotlinx.coroutines.launch

class AuthViewModel(private val networkModule: NetworkModule) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    fun onNameChange(newValue: String) { _name.value = newValue }

    private val _id = mutableStateOf("")
    val id: State<String> = _id

    fun onIdChange(newValue: String) { _id.value = newValue }

    private val _pw = mutableStateOf("")
    val pw: State<String> = _pw

    fun onPwChange(newValue: String) { _pw.value = newValue }

    private val _pwConfirm = mutableStateOf("")
    val pwConfirm: State<String> = _pwConfirm

    fun onPwConfirmChange(newValue: String) { _pwConfirm.value = newValue }

    fun login(onSuccess: (Boolean) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = networkModule.authApi.login(
                    LoginRequest(
                        loginId = id.value,
                        password = pw.value,
                    )
                )

                val accessToken = response.accessToken
                val refreshToken = response.refreshToken

                if (!accessToken.isNullOrBlank()) {
                    networkModule.tokenDataStore.saveTokens(accessToken, refreshToken)
                    Log.d("Login", "로그인 성공! Token: $accessToken")

                    onSuccess(false)
                } else {
                    Log.e("Login", "토큰이 비어있음")
                    onError()
                }
            } catch (e: Exception) {
                Log.e("Login", "로그인 통신 실패", e)
                onError()
            }
        }
    }

    fun signUp(
        loginId: String,
        password: String,
        name: String,
        nickname: String,
        email: String,
        gender: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 🚨 주의: SignUpRequest DTO의 실제 파라미터 이름에 맞춰서 값을 넣어주세요!
                val request = com.fitflow.clover.data.remote.dto.SignUpRequest(
                    loginId = loginId,
                    password = password,
                    name = name,
                    nickname = nickname,
                    email = email,
                    gender = gender
                )

                // 백엔드 API 호출!
                val response = networkModule.authApi.signUp(request)
                Log.d("SignUp", "회원가입 성공: $response")

                // 성공 시 콜백 실행 (다이얼로그 띄우기용)
                onSuccess()
            } catch (e: Exception) {
                Log.e("SignUp", "회원가입 통신 실패", e)
                onError()
            }
        }
    }
}