package com.fitflow.clover.mypage

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


// 1. 여기에 데이터 클래스를 배치합니다.
data class MyProfileModifyUiState(
    val imageUri: Uri? = null,
    val height: String = "키 선택",
    val weight: String = "몸무게 선택",
    val obesity: String = "퍼스널 컬러 선택",
    val faceShape: String = "체형 선택"
)

class MyPageViewModel : ViewModel() {

    // 🎯 외부(UI 화면)에는 읽기 전용으로 노출할 상태 데이터 (기본값 0%)
    private val _cloverProgress = MutableStateFlow(0.0f)
    val cloverProgress: StateFlow<Float> = _cloverProgress

    init {
        // 화면이 켜지자마자 DB에서 유저 점수를 불러옵니다.
        loadUserCloverScoreFromDB()
    }

    private fun loadUserCloverScoreFromDB() {
        // [여기에 나중에 실제 DB 연동 코드가 들어갑니다]
        // 예시: 파이어베이스나 레트로핏으로 유저의 현재 활동 점수를 가져옴
        val userCurrentScore = 350  // DB에서 읽어온 점수 예시
        val maxScore = 1000         // 만점 기준

        // 🎯 비율 계산 (350 / 1000 = 0.35f) 후 데이터 업데이트!
        _cloverProgress.value = userCurrentScore.toFloat() / maxScore
    }

}
class MyProfileModifyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyProfileModifyUiState())
    val uiState: StateFlow<MyProfileModifyUiState> = _uiState.asStateFlow()

    // 💡 값 변경 함수들
    fun onImageSelected(uri: Uri) { _uiState.value = _uiState.value.copy(imageUri = uri) }
    fun onHeightSelected(height: String) { _uiState.value = _uiState.value.copy(height = height) }
    fun onWeightSelected(weight: String) { _uiState.value = _uiState.value.copy(weight = weight) }
    fun onObesitySelected(obesity: String) { _uiState.value = _uiState.value.copy(obesity = obesity) }
    fun onFaceShapeSelected(faceShape: String) { _uiState.value = _uiState.value.copy(faceShape = faceShape) }

    // 🎯 [핵심] 데이터 저장 로직
    fun saveProfileChanges(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        // 여기에 코루틴을 사용하여 서버 통신(Retrofit) 또는 로컬 저장(DataStore/Room)을 수행합니다.
        // viewModelScope.launch {
        //     repository.saveProfile(currentState)
        //     onSuccess()
        // }

        // 당장 테스트를 위해 성공 콜백만 실행합니다.
        onSuccess()
    }
}