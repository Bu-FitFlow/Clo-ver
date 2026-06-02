package com.fitflow.clover.presentation.diagnosis.personalcolor

data class PersonalColorUiState(
    val currentQuestionIndex: Int = 0,
    val questions: List<PersonalColorQuestion> = emptyList(),
    val selectedAnswers: Map<Int, PersonalColorTone> = emptyMap(),
    val result: PersonalColorResultUiModel = PersonalColorResultUiModel(
        personalColor = "",
        resultTitle = "",
        resultRecommend = ""
    )
) {
    val currentQuestion: PersonalColorQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val isNextEnabled: Boolean
        get() = selectedAnswers.containsKey(currentQuestionIndex)

    val isLastQuestion: Boolean
        get() = questions.isNotEmpty() && currentQuestionIndex == questions.lastIndex
}

data class PersonalColorQuestion(
    val title: String,
    val options: List<PersonalColorOption>
)

data class PersonalColorOption(
    val label: String,
    val tone: PersonalColorTone
)

enum class PersonalColorTone {
    WARM,
    COOL
}