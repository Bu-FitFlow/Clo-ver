package com.fitflow.clover.presentation.diagnosis.personalcolor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PersonalColorResultScreen(
    result: PersonalColorResultUiModel,
    onMoveToMain: () -> Unit
) {
    val progress = remember { mutableStateOf(0.05f) }

    LaunchedEffect(Unit) {
        while (progress.value < 1f) {
            delay(35)
            progress.value = (progress.value + 0.03f).coerceAtMost(1f)
        }

        delay(350)
        onMoveToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        PersonalColorLoadingResultContent(
            result = result,
            progress = progress.value
        )
    }
}

@Composable
private fun PersonalColorLoadingResultContent(
    result: PersonalColorResultUiModel,
    progress: Float
) {
    Column(
        modifier = Modifier.padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = result.resultTitle.ifBlank {
                "00님은\n겨울 [쿨톤] 계열이\n잘 어울리는 타입이에요!"
            },
            color = Color.Black,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp
        )

        Spacer(modifier = Modifier.height(70.dp))

        CloverPersonalColorProgressBar(
            progress = progress
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "이제 00님과 가장 잘 어울리는 옷을 찾으러 갈까요?",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CloverPersonalColorProgressBar(
    progress: Float
) {
    val safeProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .width(230.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(999.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(safeProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0xFF98DB82))
        )
    }
}