package com.fitflow.clover.presentation.diagnosis

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultUiModel
import kotlin.math.pow

class DiagnosisViewModel {

    var uiState = mutableStateOf(DiagnosisUiState())
        private set

    val heightOptions: List<Int> = (140..200 step 5).toList()
    val weightOptions: List<Int> = (40..120 step 2).toList()

    fun selectGender(gender: DiagnosisGender) {
        uiState.value = uiState.value.copy(
            selectedGender = gender
        )
    }

    fun selectHeight(heightCm: Int) {
        uiState.value = uiState.value.copy(
            selectedHeightCm = heightCm
        )
    }

    fun selectWeight(weightKg: Int) {
        uiState.value = uiState.value.copy(
            selectedWeightKg = weightKg
        )
    }

    fun onBodyPhotoCaptured(bitmap: Bitmap?) {
        if (bitmap == null) return

        uiState.value = uiState.value.copy(
            bodyPhotoBitmap = bitmap,
            isBodyAnalyzing = true,
            bodyResult = null
        )
    }

    fun completeBodyAnalysis() {
        val state = uiState.value

        if (!state.isBodyAnalyzing || state.bodyPhotoBitmap == null) {
            return
        }

        uiState.value = state.copy(
            isBodyAnalyzing = false,
            bodyResult = createMockBodyResult(state)
        )
    }

    fun resetBodyPhoto() {
        uiState.value = uiState.value.copy(
            bodyPhotoBitmap = null,
            isBodyAnalyzing = false,
            bodyResult = null
        )
    }

    fun onPersonalColorPhotoCaptured(bitmap: Bitmap?) {
        if (bitmap == null) return

        uiState.value = uiState.value.copy(
            personalColorPhotoBitmap = bitmap,
            isPersonalColorAnalyzing = true,
            personalColorResult = null
        )
    }

    fun completePersonalColorAnalysis() {
        val state = uiState.value

        if (!state.isPersonalColorAnalyzing || state.personalColorPhotoBitmap == null) {
            return
        }

        uiState.value = state.copy(
            isPersonalColorAnalyzing = false,
            personalColorResult = PersonalColorResultUiModel(
                personalColor = "WINTER_COOL",
                resultTitle = "00님은\n겨울 [쿨톤] 계열이\n잘 어울리는 타입이에요!",
                resultRecommend = "선명한 색감, 차가운 톤, 대비감이 있는 스타일이 잘 어울려요."
            )
        )
    }

    fun resetPersonalColorPhoto() {
        uiState.value = uiState.value.copy(
            personalColorPhotoBitmap = null,
            isPersonalColorAnalyzing = false,
            personalColorResult = null
        )
    }

    private fun createMockBodyResult(state: DiagnosisUiState): BodyAnalysisResult {
        val heightMeter = (state.selectedHeightCm ?: 170) / 100.0
        val weightKg = state.selectedWeightKg ?: 60
        val bmi = weightKg / heightMeter.pow(2.0)

        return when {
            bmi < 18.5 -> {
                BodyAnalysisResult(
                    bodyType = "SLIM",
                    title = "00님의 체형은 슬림형에 가까워요.",
                    description = "전체적으로 가벼운 실루엣이 잘 어울리는 체형으로 분석되었어요.",
                    recommendMessage = "너무 큰 오버핏보다는 적당히 라인이 잡힌 스타일을 추천해요."
                )
            }

            bmi < 23.0 -> {
                BodyAnalysisResult(
                    bodyType = "BALANCED",
                    title = "00님의 체형은 균형형에 가까워요.",
                    description = "상체와 하체의 비율이 안정적으로 보이는 체형으로 분석되었어요.",
                    recommendMessage = "기본핏, 세미오버핏, 레이어드 스타일을 자연스럽게 활용하기 좋아요."
                )
            }

            bmi < 25.0 -> {
                BodyAnalysisResult(
                    bodyType = "NATURAL",
                    title = "00님의 체형은 내추럴형에 가까워요.",
                    description = "전체적인 골격감과 실루엣이 자연스럽게 드러나는 체형으로 분석되었어요.",
                    recommendMessage = "직선적인 라인과 깔끔한 아우터 중심의 스타일을 추천해요."
                )
            }

            else -> {
                BodyAnalysisResult(
                    bodyType = "STRAIGHT",
                    title = "00님의 체형은 스트레이트형에 가까워요.",
                    description = "상체 중심의 안정감 있는 실루엣이 돋보이는 체형으로 분석되었어요.",
                    recommendMessage = "세로 라인이 살아나는 상의와 깔끔한 팬츠 조합을 추천해요."
                )
            }
        }
    }
}