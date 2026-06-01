package com.fitflow.clover.presentation.diagnosis.personalcolor

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.presentation.diagnosis.DiagnosisViewModel
import kotlinx.coroutines.delay

@Composable
fun PersonalColorScreen(
    viewModel: DiagnosisViewModel,
    onMoveToResult: () -> Unit,
    onMoveToRetry: () -> Unit,
    onMoveToMain: () -> Unit
) {
    val uiState by viewModel.uiState

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        viewModel.onPersonalColorPhotoCaptured(bitmap)
    }

    LaunchedEffect(uiState.isPersonalColorAnalyzing, uiState.personalColorPhotoBitmap) {
        if (uiState.isPersonalColorAnalyzing && uiState.personalColorPhotoBitmap != null) {
            delay(900)

            val success = viewModel.completePersonalColorAnalysis()

            if (success) {
                onMoveToResult()
            } else {
                onMoveToRetry()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 26.dp)
    ) {
        PersonalColorTopBar(
            title = "퍼스널 컬러 진단",
            onBack = onMoveToMain,
            onSkip = onMoveToMain
        )

        Spacer(modifier = Modifier.height(112.dp))

        Text(
            text = "정면으로 보고 사진을 찍어주세요.\n얼굴이 잘 보이게 촬영해주세요.",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFF3B30),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        PersonalColorPhotoCaptureBox(
            bitmap = uiState.personalColorPhotoBitmap,
            isLoading = uiState.isPersonalColorAnalyzing,
            onClick = {
                cameraLauncher.launch(null)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            uiState.isPersonalColorAnalyzing -> {
                Text(
                    text = "퍼스널 컬러 데이터를 분석하고 있어요.",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            uiState.personalColorAnalysisErrorMessage != null -> {
                Text(
                    text = uiState.personalColorAnalysisErrorMessage.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFF3B30),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PersonalColorTopBar(
    title: String,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onBack,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "‹",
                color = Color.Black,
                fontSize = 36.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = title,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onSkip,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "건너뛰기",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PersonalColorPhotoCaptureBox(
    bitmap: Bitmap?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 54.dp)
            .height(272.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    color = Color(0xFF98DB82)
                )
            }

            bitmap != null -> {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "촬영한 퍼스널 컬러 진단 사진",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            else -> {
                PersonalColorPlusIcon()
            }
        }
    }
}

@Composable
private fun PersonalColorPlusIcon() {
    Canvas(
        modifier = Modifier.size(74.dp)
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(size.width / 2f, 0f),
            end = Offset(size.width / 2f, size.height),
            strokeWidth = 1.2.dp.toPx()
        )

        drawLine(
            color = Color.Black,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = 1.2.dp.toPx()
        )
    }
}