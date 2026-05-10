package com.fitflow.clover.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    // 1. 이름 상태
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    fun onNameChange(newValue: String) {
        _name.value = newValue
    }

    // 2. 아이디 상태
    private val _id = mutableStateOf("")
    val id: State<String> = _id

    fun onIdChange(newValue: String) {
        _id.value = newValue
    }

    // 3. 비밀번호 상태
    private val _pw = mutableStateOf("")
    val pw: State<String> = _pw

    fun onPwChange(newValue: String) {
        _pw.value = newValue
    }

    // 4. 비밀번호 확인 상태
    private val _pwConfirm = mutableStateOf("")
    val pwConfirm: State<String> = _pwConfirm

    fun onPwConfirmChange(newValue: String) {
        _pwConfirm.value = newValue
    }
}