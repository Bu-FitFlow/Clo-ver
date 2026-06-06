package com.fitflow.clover.mypage.setup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream

class MyProfileModifyViewModel : ViewModel() {

    data class ProfileUiState(
        val imageUri: Uri? = null,
        val height: String = "키 선택",
        val weight: String = "몸무게 선택",
        val obesity: String = "퍼스널 컬러 선택",
        val faceShape: String = "체형 선택"
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var isInitialized = false

    fun setInitialProfile(
        imageUri: String?,
        height: String,
        weight: String,
        personalColor: String,
        bodyType: String
    ) {
        if (isInitialized) return
        isInitialized = true

        _uiState.value = ProfileUiState(
            imageUri = imageUri?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) },
            height = height.takeUnless { it.contains("정보 없음") } ?: "키 선택",
            weight = weight.takeUnless { it.contains("정보 없음") } ?: "몸무게 선택",
            obesity = personalColor.takeUnless { it.contains("정보 없음") } ?: "퍼스널 컬러 선택",
            faceShape = bodyType.takeUnless { it.contains("정보 없음") } ?: "체형 선택"
        )
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun onHeightSelected(height: String) {
        _uiState.value = _uiState.value.copy(height = height)
    }

    fun onWeightSelected(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun onObesitySelected(obesity: String) {
        _uiState.value = _uiState.value.copy(obesity = obesity)
    }

    fun onFaceShapeSelected(faceShape: String) {
        _uiState.value = _uiState.value.copy(faceShape = faceShape)
    }

    fun saveProfileData(context: Context): String? {
        val currentState = _uiState.value
        val savedImageFile = currentState.imageUri?.let { uri ->
            saveImageToInternalStorage(context, uri)
        }
        return savedImageFile?.let { Uri.fromFile(it).toString() }
            ?: currentState.imageUri?.toString()
    }

    private fun saveImageToInternalStorage(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            null
        }
    }
}
