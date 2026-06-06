package com.fitflow.clover.mypage

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fitflow.clover.presentation.diagnosis.DiagnosisUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyPageViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    fun loadMyPage(context: Context) {
        val prefs = context.getSharedPreferences("my_page_profile", Context.MODE_PRIVATE)

        val savedName = prefs.getString("name", "") ?: ""
        val savedEmail = prefs.getString("email", "") ?: ""
        val savedNickname = prefs.getString("nickname", "") ?: ""
        val savedLoginId = prefs.getString("loginId", "") ?: ""

        val savedProfileImage = prefs.getString("profileImageModel", null)
        val savedHeight = prefs.getString("heightLabel", "선택 안 됨") ?: "선택 안 됨"
        val savedWeight = prefs.getString("weightLabel", "선택 안 됨") ?: "선택 안 됨"
        val savedPersonalColor = prefs.getString("personalColorLabel", "선택 안 됨") ?: "선택 안 됨"
        val savedBodyType = prefs.getString("bodyTypeLabel", "선택 안 됨") ?: "선택 안 됨"

        _uiState.value = _uiState.value.copy(
            name = savedName.ifBlank { "사용자" },
            email = savedEmail.ifBlank { "user@email.com" },
            nickname = savedNickname.ifBlank { "CLOVER" },
            loginId = savedLoginId.ifBlank { "clover_user" },
            displayLoginId = savedLoginId.ifBlank { savedEmail.ifBlank { "clover_user" } },
            profileImageModel = savedProfileImage,
            heightLabel = savedHeight,
            weightLabel = savedWeight,
            personalColorLabel = savedPersonalColor,
            bodyTypeLabel = savedBodyType,
            cloverProgress = 0.72f,
            myProducts = createDefaultProducts(),
            myPosts = createDefaultPosts()
        )
    }

    fun updateNickname(context: Context, nickname: String) {
        val newNickname = nickname.trim()

        context.getSharedPreferences("my_page_profile", Context.MODE_PRIVATE)
            .edit()
            .putString("nickname", newNickname)
            .apply()

        _uiState.value = _uiState.value.copy(
            nickname = newNickname
        )
    }

    fun updateProfileFromEdit(
        heightLabel: String,
        weightLabel: String,
        personalColorLabel: String,
        bodyTypeLabel: String,
        profileImageUri: String?
    ) {
        _uiState.value = _uiState.value.copy(
            heightLabel = heightLabel.ifBlank { "선택 안 됨" },
            weightLabel = weightLabel.ifBlank { "선택 안 됨" },
            personalColorLabel = personalColorLabel.ifBlank { "선택 안 됨" },
            bodyTypeLabel = bodyTypeLabel.ifBlank { "선택 안 됨" },
            profileImageModel = profileImageUri ?: _uiState.value.profileImageModel
        )
    }

    fun syncDiagnosisState(diagnosisUiState: DiagnosisUiState) {
        val height = withUnit(diagnosisUiState.selectedHeightCm, "cm")
        val weight = withUnit(diagnosisUiState.selectedWeightKg, "kg")
        val bodyType = normalizeText(diagnosisUiState.bodyResult)
        val personalColor = normalizeText(diagnosisUiState.personalColorResult)

        _uiState.value = _uiState.value.copy(
            heightLabel = height.ifBlank { _uiState.value.heightLabel },
            weightLabel = weight.ifBlank { _uiState.value.weightLabel },
            bodyTypeLabel = bodyType.ifBlank { _uiState.value.bodyTypeLabel },
            personalColorLabel = personalColor.ifBlank { _uiState.value.personalColorLabel }
        )
    }

    private fun withUnit(value: Any?, unit: String): String {
        val text = normalizeText(value)

        if (text.isBlank()) {
            return ""
        }

        return if (text.endsWith(unit)) {
            text
        } else {
            "$text$unit"
        }
    }

    private fun normalizeText(value: Any?): String {
        val text = value?.toString()?.trim().orEmpty()

        return if (text.isBlank() || text == "null") {
            ""
        } else {
            text
        }
    }

    private fun createDefaultProducts(): List<MyPageProductItem> {
        return listOf(
            MyPageProductItem(
                id = 1,
                thumbnailImageUrl = "",
                name = "등록한 상품",
                price = "0원",
                postStatus = "판매중"
            ),
            MyPageProductItem(
                id = 2,
                thumbnailImageUrl = "",
                name = "관심 상품",
                price = "0원",
                postStatus = "판매중"
            )
        )
    }

    private fun createDefaultPosts(): List<MyPagePostItem> {
        return listOf(
            MyPagePostItem(
                id = 1,
                category = "전체",
                title = "작성한 게시글",
                content = "마이페이지 게시글 예시입니다.",
                commentCount = 0,
                createdAt = "방금 전"
            ),
            MyPagePostItem(
                id = 2,
                category = "자유",
                title = "내 게시글 관리",
                content = "작성한 게시글을 확인할 수 있습니다.",
                commentCount = 0,
                createdAt = "방금 전"
            )
        )
    }
}