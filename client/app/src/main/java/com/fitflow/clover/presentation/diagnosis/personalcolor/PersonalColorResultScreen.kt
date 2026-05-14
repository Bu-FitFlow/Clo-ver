package com.fitflow.clover.presentation.diagnosis.personalcolor

import androidx.compose.foundation.background
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
import androidx.compose.material3.TextButton
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
    val isLoadingFinished = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (progress.value < 1f) {
            delay(35)
            progress.value = (progress.value + 0.03f).coerceAtMost(1f)
        }

        delay(300)
        isLoadingFinished.value = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        if (!isLoadingFinished.value) {
            PersonalColorLoadingResultContent(
                result = result,
                progress = progress.value
            )
        } else {
            PersonalColorCompletedResultContent(
                result = result,
                onMoveToMain = onMoveToMain
            )
        }
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
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(68.dp))

        CloverPersonalColorProgressBar(
            progress = progress
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "이제 00님과 가장 잘 어울리는 옷을 찾고 있어요.",
            color = Color.Black,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PersonalColorCompletedResultContent(
    result: PersonalColorResultUiModel,
    onMoveToMain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = result.resultTitle.ifBlank {
                "00님은\n겨울 [쿨톤] 계열이\n잘 어울리는 타입이에요!"
            },
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = result.personalColor.ifBlank {
                "WINTER_COOL"
            },
            color = Color(0xFF5FAE4F),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = result.resultRecommend.ifBlank {
                "선명한 색감, 차가운 톤, 대비감이 있는 스타일이 잘 어울려요."
            },
            color = Color(0xFF555555),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        TextButton(
            onClick = onMoveToMain
        ) {
            Text(
                text = "메인으로",
                color = Color.Black,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CloverPersonalColorProgressBar(
    progress: Float
) {
    val safeProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .width(170.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFFE8E8E8))
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