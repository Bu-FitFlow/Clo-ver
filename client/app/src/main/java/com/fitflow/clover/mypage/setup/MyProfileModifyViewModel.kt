package com.fitflow.clover.mypage.setup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MyProfileModifyViewModel : ViewModel() {

    // 💡 UI 상태를 하나의 데이터 클래스로 묶어서 관리하면 깔끔합니다.
    data class ProfileUiState(
        val imageUri: Uri? = null,
        val height: String = "키 선택",
        val weight: String = "몸무게 선택",
        val obesity: String = "퍼스널 컬러 선택",
        val faceShape: String = "체형 선택"
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // 1. 사용자가 이미지를 선택했을 때 호출할 함수
    fun onImageSelected(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    // 2. 키 선택 시 호출
    fun onHeightSelected(height: String) {
        _uiState.value = _uiState.value.copy(height = height)
    }

    // 3. 몸무게 선택 시 호출
    fun onWeightSelected(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    // 4. 퍼스널컬러 선택 시 호출
    fun onObesitySelected(obesity: String) {
        _uiState.value = _uiState.value.copy(obesity = obesity)
    }

    // 5. 체형 호출
    fun onFaceShapeSelected(faceShape: String) {
        _uiState.value = _uiState.value.copy(faceShape = faceShape)
    }

    // 6. [완료] 버튼을 눌렀을 때 최종 저장 로직을 수행할 함수
    fun saveProfileData(context: Context) {
        viewModelScope.launch {
            val currentState = _uiState.value

            // ⚠️ PickVisualMedia의 Uri는 임시 권한이므로 내부 저장소에 파일로 복사해두는 것이 안전합니다.
            val savedImageFile = currentState.imageUri?.let { uri ->
                saveImageToInternalStorage(context, uri)
            }

            // TODO: 여기서 Repository를 호출하여 서버나 Local DB(DataStore/Room)에 최종 저장작업을 진행합니다.
            // 예시: profileRepository.updateProfile(savedImageFile, currentState.height, currentState.weight ...)

            println("프로필 저장 완료: 키=${currentState.height}, 몸무게=${currentState.weight}, 이미지경로=${savedImageFile?.absolutePath}")
        }
    }

    // 📸 갤러리 임시 Uri 조각을 앱 내부 저장소(filesDir)에 실제 파일로 복사하는 치트키 메서드
    private fun saveImageToInternalStorage(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.filesDir, "profile_image.jpg") // 항상 덮어씌워지도록 고정명칭 생성
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // MyProfileModifyViewModel.kt

    // [수정된 저장 함수]
    fun saveProfileChanges(context: Context): Boolean {
        val currentState = _uiState.value
        val uri = currentState.imageUri

        return try {
            // 1. 이미지가 선택되어 있다면 내부 저장소에 저장 시도
            if (uri != null) {
                val savedImageFile = saveImageToInternalStorage(context, uri)
                if (savedImageFile == null) return false // 파일 저장 실패 시 false
            }

            // 2. [추가] 서버나 DB에 키, 몸무게 등 나머지 정보 저장 로직
            // 예: repository.updateProfile(currentState)

            true // 모든 과정이 성공하면 true 반환
        } catch (e: Exception) {
            e.printStackTrace()
            false // 오류 발생 시 false 반환
        }
    }
}