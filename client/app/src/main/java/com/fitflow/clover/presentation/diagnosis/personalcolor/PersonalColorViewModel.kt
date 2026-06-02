package com.fitflow.clover.presentation.diagnosis.personalcolor

import androidx.compose.runtime.mutableStateOf

class PersonalColorViewModel {

    var uiState = mutableStateOf(
        PersonalColorUiState(
            questions = personalColorQuestions
        )
    )
        private set

    fun selectAnswer(
        questionIndex: Int,
        tone: PersonalColorTone
    ) {
        val copiedAnswers = uiState.value.selectedAnswers.toMutableMap()
        copiedAnswers[questionIndex] = tone

        uiState.value = uiState.value.copy(
            selectedAnswers = copiedAnswers
        )
    }

    fun moveNext(): PersonalColorMoveResult {
        val state = uiState.value

        if (!state.isNextEnabled) {
            return PersonalColorMoveResult.Stay
        }

        if (!state.isLastQuestion) {
            uiState.value = state.copy(
                currentQuestionIndex = state.currentQuestionIndex + 1
            )
            return PersonalColorMoveResult.Stay
        }

        return calculateResult()
    }

    fun moveBack(): Boolean {
        val state = uiState.value

        return if (state.currentQuestionIndex > 0) {
            uiState.value = state.copy(
                currentQuestionIndex = state.currentQuestionIndex - 1
            )
            true
        } else {
            false
        }
    }

    fun reset() {
        uiState.value = PersonalColorUiState(
            questions = personalColorQuestions
        )
    }

    private fun calculateResult(): PersonalColorMoveResult {
        val state = uiState.value

        if (state.selectedAnswers.size < personalColorQuestions.size) {
            return PersonalColorMoveResult.Retry
        }

        val warmCount = state.selectedAnswers.values.count { tone ->
            tone == PersonalColorTone.WARM
        }

        val coolCount = state.selectedAnswers.values.count { tone ->
            tone == PersonalColorTone.COOL
        }

        val result = if (coolCount >= warmCount) {
            PersonalColorResultUiModel(
                personalColor = "WINTER_COOL",
                resultTitle = "00님은\n겨울 [쿨톤] 계열이\n잘 어울리는 타입이에요!",
                resultRecommend = "선명한 색감, 차가운 톤, 대비감이 있는 스타일이 잘 어울려요."
            )
        } else {
            PersonalColorResultUiModel(
                personalColor = "SPRING_WARM",
                resultTitle = "00님은\n봄 [웜톤] 계열이\n잘 어울리는 타입이에요!",
                resultRecommend = "밝고 부드러운 색감, 따뜻한 톤, 생기 있는 스타일이 잘 어울려요."
            )
        }

        uiState.value = state.copy(
            result = result
        )

        return PersonalColorMoveResult.Result
    }
}

sealed class PersonalColorMoveResult {
    data object Stay : PersonalColorMoveResult()
    data object Result : PersonalColorMoveResult()
    data object Retry : PersonalColorMoveResult()
}

private val personalColorQuestions = listOf(
    PersonalColorQuestion(
        title = "자연광 아래에서 얼굴이 더 잘 어울리는 느낌은?",
        options = listOf(
            PersonalColorOption("A. 따뜻하고 부드러운 분위기", PersonalColorTone.WARM),
            PersonalColorOption("B. 또렷하고 시원한 분위기", PersonalColorTone.COOL)
        )
    ),
    PersonalColorQuestion(
        title = "악세서리를 착용했을 때 더 자연스러운 쪽은?",
        options = listOf(
            PersonalColorOption("A. 골드 계열", PersonalColorTone.WARM),
            PersonalColorOption("B. 실버 계열", PersonalColorTone.COOL)
        )
    ),
    PersonalColorQuestion(
        title = "밝은 색 옷을 입었을 때 느낌은?",
        options = listOf(
            PersonalColorOption("A. 얼굴이 화사해 보인다.", PersonalColorTone.WARM),
            PersonalColorOption("B. 다소 부담스러울 때가 있다.", PersonalColorTone.COOL)
        )
    ),
    PersonalColorQuestion(
        title = "어두운 색 옷을 입었을 때는?",
        options = listOf(
            PersonalColorOption("A. 얼굴이 또렷해 보인다.", PersonalColorTone.COOL),
            PersonalColorOption("B. 얼굴이 칙칙해 보인다.", PersonalColorTone.WARM)
        )
    ),
    PersonalColorQuestion(
        title = "선명한 색 옷을 입었을 때 느낌은?",
        options = listOf(
            PersonalColorOption("A. 생기가 살아난다.", PersonalColorTone.COOL),
            PersonalColorOption("B. 다소 과하게 느껴진다.", PersonalColorTone.WARM)
        )
    ),
    PersonalColorQuestion(
        title = "차분한 색 옷을 입었을 때 느낌은?",
        options = listOf(
            PersonalColorOption("A. 안정적이고 자연스럽다.", PersonalColorTone.WARM),
            PersonalColorOption("B. 조금 답답하게 느껴진다.", PersonalColorTone.COOL)
        )
    ),
    PersonalColorQuestion(
        title = "평소 좀 더 자주 손이 가는 톤은?",
        options = listOf(
            PersonalColorOption("A. 밝고 맑은 톤", PersonalColorTone.WARM),
            PersonalColorOption("B. 깊고 차분한 색", PersonalColorTone.COOL)
        )
    )
)