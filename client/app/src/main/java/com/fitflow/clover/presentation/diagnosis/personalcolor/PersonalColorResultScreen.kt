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
    userDisplayName: String = "사용자",
    onMoveToMain: () -> Unit
) {
    val progress = remember {
        mutableStateOf(0.05f)
    }

    val safeUserName = userDisplayName
        .trim()
        .takeIf { it.isNotBlank() }
        ?: "사용자"

    LaunchedEffect(result.personalColor) {
        while (progress.value < 1f) {
            delay(35)
            progress.value = (progress.value + 0.03f).coerceAtMost(1f)
        }

        delay(450)
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
            userDisplayName = safeUserName,
            progress = progress.value
        )
    }
}

@Composable
private fun PersonalColorLoadingResultContent(
    result: PersonalColorResultUiModel,
    userDisplayName: String,
    progress: Float
) {
    val seasonLabel = result.personalColor.toPersonalColorSeasonLabel()

    Column(
        modifier = Modifier.padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = result.resultTitle.ifBlank {
                "${userDisplayName}님은\n${seasonLabel.koreanName} 계열이\n잘 어울리는 타입이에요!"
            },
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 42.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = seasonLabel.fullName,
            color = Color(0xFF5FAE4F),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = result.resultRecommend,
            color = Color(0xFF555555),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(70.dp))

        CloverPersonalColorProgressBar(
            progress = progress
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "이제 ${userDisplayName}님과 가장 잘 어울리는 옷을 찾으러 갈까요?",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
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

private data class PersonalColorSeasonLabel(
    val koreanName: String,
    val fullName: String
)

private fun String.toPersonalColorSeasonLabel(): PersonalColorSeasonLabel {
    return when (trim().uppercase()) {
        "SPRING_WARM" -> {
            PersonalColorSeasonLabel(
                koreanName = "봄 웜톤",
                fullName = "봄 웜톤 (Spring Warm)"
            )
        }

        "SUMMER_COOL" -> {
            PersonalColorSeasonLabel(
                koreanName = "여름 쿨톤",
                fullName = "여름 쿨톤 (Summer Cool)"
            )
        }

        "AUTUMN_WARM" -> {
            PersonalColorSeasonLabel(
                koreanName = "가을 웜톤",
                fullName = "가을 웜톤 (Autumn Warm)"
            )
        }

        "WINTER_COOL" -> {
            PersonalColorSeasonLabel(
                koreanName = "겨울 쿨톤",
                fullName = "겨울 쿨톤 (Winter Cool)"
            )
        }

        else -> {
            PersonalColorSeasonLabel(
                koreanName = "겨울 쿨톤",
                fullName = "겨울 쿨톤 (Winter Cool)"
            )
        }
    }
}