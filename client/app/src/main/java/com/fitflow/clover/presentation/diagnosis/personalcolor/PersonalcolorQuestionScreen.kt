package com.fitflow.clover.presentation.diagnosis.personalcolor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PersonalColorQuestionScreen(
    viewModel: PersonalColorViewModel,
    onMoveToResult: () -> Unit,
    onMoveToRetry: () -> Unit,
    onMoveToMain: () -> Unit
) {
    val uiState by viewModel.uiState
    val question = uiState.currentQuestion

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        val moved = viewModel.moveBack()
                        if (!moved) {
                            onMoveToMain()
                        }
                    }
                ) {
                    Text(
                        text = "‹",
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "퍼스널 컬러 진단",
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onMoveToMain
                ) {
                    Text(
                        text = "건너뛰기",
                        color = Color.Black
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
            ) {
                if (question != null) {
                    PersonalColorQuestionItem(
                        question = question,
                        selectedTone = uiState.selectedAnswers[uiState.currentQuestionIndex],
                        onSelect = { tone ->
                            viewModel.selectAnswer(uiState.currentQuestionIndex, tone)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    when (viewModel.moveNext()) {
                        PersonalColorMoveResult.Stay -> Unit
                        PersonalColorMoveResult.Result -> onMoveToResult()
                        PersonalColorMoveResult.Retry -> onMoveToRetry()
                    }
                },
                enabled = uiState.isNextEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 42.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF98DB82),
                    disabledContainerColor = Color(0xFFD9D9D9),
                    contentColor = Color.Black,
                    disabledContentColor = Color(0xFF777777)
                )
            ) {
                Text(
                    text = if (uiState.isLastQuestion) "결과 확인" else "다음"
                )
            }
        }
    }
}