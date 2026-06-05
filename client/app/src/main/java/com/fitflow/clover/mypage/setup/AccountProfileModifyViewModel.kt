package com.fitflow.clover.mypage.setup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 💡 UI 파일에서 부르는 이름과 똑같이 'AccountProfileModifyViewModel'로 매칭했습니다!
class AccountProfileModifyViewModel : ViewModel() {

    // 💡 유저 계정 정보를 담는 데이터 클래스
    data class AccountUiState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        // 🆕 수정 화면에서 입력창에 실시간으로 적히는 글자를 임시 저장할 필드
        val inputNickname: String = ""
    )

    // 💡 UI 파일에서 viewModel.uiState로 접근할 수 있도록 변수명을 소문자 uiState로 정확히 맞췄습니다.
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        // 💡 [초기값 세팅] 지금은 화면에 띄우기 위한 테스트용 더미 데이터를 넣어둡니다.
        // 나중에 이 부분에서 서버나 DB의 실제 유저 정보를 로드하게 됩니다.
        loadUserProfile()
    }



    private fun loadUserProfile() {
        _uiState.value = AccountUiState(
            name = "000",
            nickname = "clover",
            email = "clover@naver.com",
            inputNickname = "clover"
        )
    }
    // 🆕 2. 사용자가 수정 창에서 타이핑할 때마다 호출되는 함수
    fun onNicknameChanged(newNickname: String) {
        _uiState.value = _uiState.value.copy(inputNickname = newNickname)
    }

    // 🆕 3. [완료] 버튼을 눌렀을 때 임시 입력값을 최종 닉네임으로 반영하는 함수
    fun saveNickname() {
        val currentInput = _uiState.value.inputNickname
        _uiState.value = _uiState.value.copy(
            nickname = currentInput // 최종 닉네임 확정 🎯
        )
        // TODO: 나중에 여기서 profileRepository.updateNickname(currentInput) 처럼 서버에 저장하시면 됩니다.
    }
}