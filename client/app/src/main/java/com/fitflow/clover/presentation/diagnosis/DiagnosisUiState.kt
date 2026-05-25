package com.fitflow.clover.presentation.diagnosis

import android.graphics.Bitmap
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultUiModel

enum class DiagnosisGender(
    val label: String
) {
    MALE("남자"),
    FEMALE("여자")
}

data class BodyAnalysisResult(
    val bodyType: String,
    val title: String,
    val description: String,
    val recommendMessage: String
)

data class DiagnosisUiState(
    val userDisplayName: String = "사용자",

    val selectedGender: DiagnosisGender? = null,
    val selectedHeightCm: Int? = null,
    val selectedWeightKg: Int? = null,

    val bodyPhotoBitmap: Bitmap? = null,
    val isBodyAnalyzing: Boolean = false,
    val bodyResult: BodyAnalysisResult? = null,
    val bodyAnalysisErrorMessage: String? = null,

    val personalColorPhotoBitmap: Bitmap? = null,
    val isPersonalColorAnalyzing: Boolean = false,
    val personalColorResult: PersonalColorResultUiModel? = null,
    val personalColorAnalysisErrorMessage: String? = null
) {
    val isInfoCompleted: Boolean
        get() = selectedGender != null &&
                selectedHeightCm != null &&
                selectedWeightKg != null

    val isBodyResultReady: Boolean
        get() = bodyPhotoBitmap != null &&
                bodyResult != null &&
                !isBodyAnalyzing &&
                bodyAnalysisErrorMessage == null

    val isPersonalColorResultReady: Boolean
        get() = personalColorPhotoBitmap != null &&
                personalColorResult != null &&
                !isPersonalColorAnalyzing &&
                personalColorAnalysisErrorMessage == null
}