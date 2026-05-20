package com.fitflow.clover.presentation.diagnosis

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private enum class BodyAnalysisStep {
    USER_INFO,
    BODY_CAMERA,
    BODY_RESULT
}

@Composable
fun BodyAnalysisScreen(
    viewModel: DiagnosisViewModel,
    onMoveToPersonalColor: () -> Unit,
    onMoveToMain: () -> Unit
) {
    val uiState by viewModel.uiState
    var currentStep by remember { mutableStateOf(BodyAnalysisStep.USER_INFO) }

    LaunchedEffect(uiState.isBodyAnalyzing, uiState.bodyPhotoBitmap) {
        if (uiState.isBodyAnalyzing && uiState.bodyPhotoBitmap != null) {
            delay(900)
            viewModel.completeBodyAnalysis()
            currentStep = BodyAnalysisStep.BODY_RESULT
        }
    }

    when (currentStep) {
        BodyAnalysisStep.USER_INFO -> {
            BodyUserInfoContent(
                uiState = uiState,
                heightOptions = viewModel.heightOptions,
                weightOptions = viewModel.weightOptions,
                onSelectGender = viewModel::selectGender,
                onSelectHeight = viewModel::selectHeight,
                onSelectWeight = viewModel::selectWeight,
                onNext = {
                    currentStep = BodyAnalysisStep.BODY_CAMERA
                },
                onBack = onMoveToMain,
                onSkip = onMoveToPersonalColor
            )
        }

        BodyAnalysisStep.BODY_CAMERA -> {
            BodyCameraContent(
                uiState = uiState,
                onPhotoCaptured = viewModel::onBodyPhotoCaptured,
                onBack = {
                    currentStep = BodyAnalysisStep.USER_INFO
                },
                onSkip = onMoveToPersonalColor
            )
        }

        BodyAnalysisStep.BODY_RESULT -> {
            BodyResultContent(
                uiState = uiState,
                onBack = {
                    currentStep = BodyAnalysisStep.BODY_CAMERA
                },
                onMoveToPersonalColor = onMoveToPersonalColor
            )
        }
    }
}

@Composable
private fun BodyUserInfoContent(
    uiState: DiagnosisUiState,
    heightOptions: List<Int>,
    weightOptions: List<Int>,
    onSelectGender: (DiagnosisGender) -> Unit,
    onSelectHeight: (Int) -> Unit,
    onSelectWeight: (Int) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 26.dp)
    ) {
        DiagnosisTopBar(
            title = "내 정보",
            onBack = onBack,
            onSkip = onSkip
        )

        Spacer(modifier = Modifier.height(58.dp))

        Text(
            text = "성별",
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenderSelectCard(
                gender = DiagnosisGender.MALE,
                selectedGender = uiState.selectedGender,
                onClick = {
                    onSelectGender(DiagnosisGender.MALE)
                }
            )

            GenderSelectCard(
                gender = DiagnosisGender.FEMALE,
                selectedGender = uiState.selectedGender,
                onClick = {
                    onSelectGender(DiagnosisGender.FEMALE)
                }
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        NumberDropdown(
            label = "키",
            suffix = "cm",
            selectedValue = uiState.selectedHeightCm,
            options = heightOptions,
            onSelect = onSelectHeight
        )

        Spacer(modifier = Modifier.height(28.dp))

        NumberDropdown(
            label = "몸무게",
            suffix = "kg",
            selectedValue = uiState.selectedWeightKg,
            options = weightOptions,
            onSelect = onSelectWeight
        )

        Spacer(modifier = Modifier.weight(1f))

        GreenBottomButton(
            text = "다음",
            enabled = uiState.isInfoCompleted,
            onClick = onNext
        )

        Spacer(modifier = Modifier.height(34.dp))
    }
}

@Composable
private fun BodyCameraContent(
    uiState: DiagnosisUiState,
    onPhotoCaptured: (Bitmap?) -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        onPhotoCaptured(bitmap)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 26.dp)
    ) {
        DiagnosisTopBar(
            title = "체형분석",
            onBack = onBack,
            onSkip = onSkip
        )

        Spacer(modifier = Modifier.height(112.dp))

        Text(
            text = "전면으로 보고 사진을 찍어주세요.\n아닐 시 정확하지 않을 수 있습니다.",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFFF3B30),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        PhotoCaptureBox(
            bitmap = uiState.bodyPhotoBitmap,
            isLoading = uiState.isBodyAnalyzing,
            onClick = {
                cameraLauncher.launch(null)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isBodyAnalyzing) {
            Text(
                text = "체형 데이터를 분석하고 있어요.",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BodyResultContent(
    uiState: DiagnosisUiState,
    onBack: () -> Unit,
    onMoveToPersonalColor: () -> Unit
) {
    val result = uiState.bodyResult

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 26.dp)
    ) {
        DiagnosisTopBar(
            title = "체형분석",
            onBack = onBack,
            onSkip = onMoveToPersonalColor
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = result?.title ?: "체형 분석이 완료되었습니다.",
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = result?.description ?: "추천 스타일 데이터를 준비했어요.",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF555555),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = result?.recommendMessage ?: "퍼스널 컬러 진단을 이어서 진행해 주세요.",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF5FAE4F),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        GreenBottomButton(
            text = "퍼스널 진단 하러 가기",
            enabled = uiState.isBodyResultReady,
            onClick = onMoveToPersonalColor
        )

        Spacer(modifier = Modifier.height(34.dp))
    }
}

@Composable
private fun DiagnosisTopBar(
    title: String,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
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
private fun GenderSelectCard(
    gender: DiagnosisGender,
    selectedGender: DiagnosisGender?,
    onClick: () -> Unit
) {
    val isSelected = selectedGender == gender

    Column(
        modifier = Modifier
            .size(width = 126.dp, height = 188.dp)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GenderFigure(gender = gender)

        Text(
            text = gender.label,
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun GenderFigure(
    gender: DiagnosisGender
) {
    Canvas(
        modifier = Modifier.size(78.dp)
    ) {
        val black = Color.Black
        val centerX = size.width / 2f

        drawCircle(
            color = black,
            radius = 8.dp.toPx(),
            center = Offset(centerX, 10.dp.toPx())
        )

        if (gender == DiagnosisGender.MALE) {
            drawRoundRect(
                color = black,
                topLeft = Offset(centerX - 11.dp.toPx(), 24.dp.toPx()),
                size = Size(22.dp.toPx(), 34.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            drawLine(
                color = black,
                start = Offset(centerX - 20.dp.toPx(), 28.dp.toPx()),
                end = Offset(centerX - 20.dp.toPx(), 56.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = black,
                start = Offset(centerX + 20.dp.toPx(), 28.dp.toPx()),
                end = Offset(centerX + 20.dp.toPx(), 56.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = black,
                start = Offset(centerX - 6.dp.toPx(), 58.dp.toPx()),
                end = Offset(centerX - 6.dp.toPx(), 76.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = black,
                start = Offset(centerX + 6.dp.toPx(), 58.dp.toPx()),
                end = Offset(centerX + 6.dp.toPx(), 76.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )
        } else {
            val dressPath = Path().apply {
                moveTo(centerX, 24.dp.toPx())
                lineTo(centerX - 18.dp.toPx(), 58.dp.toPx())
                lineTo(centerX + 18.dp.toPx(), 58.dp.toPx())
                close()
            }

            drawPath(
                path = dressPath,
                color = black
            )

            drawLine(
                color = black,
                start = Offset(centerX - 18.dp.toPx(), 30.dp.toPx()),
                end = Offset(centerX - 28.dp.toPx(), 58.dp.toPx()),
                strokeWidth = 7.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = black,
                start = Offset(centerX + 18.dp.toPx(), 30.dp.toPx()),
                end = Offset(centerX + 28.dp.toPx(), 58.dp.toPx()),
                strokeWidth = 7.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = black,
                start = Offset(centerX - 7.dp.toPx(), 60.dp.toPx()),
                end = Offset(centerX - 7.dp.toPx(), 76.dp.toPx()),
                strokeWidth = 7.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = black,
                start = Offset(centerX + 7.dp.toPx(), 60.dp.toPx()),
                end = Offset(centerX + 7.dp.toPx(), 76.dp.toPx()),
                strokeWidth = 7.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun NumberDropdown(
    label: String,
    suffix: String,
    selectedValue: Int?,
    options: List<Int>,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = {
                expanded = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(3.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValue?.let { "$it$suffix" } ?: label,
                    color = Color.Black,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = if (expanded) "▲" else "▼",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { value ->
                DropdownMenuItem(
                    text = {
                        Text(text = "$value$suffix")
                    },
                    onClick = {
                        onSelect(value)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PhotoCaptureBox(
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
            bitmap != null -> {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "촬영한 체형 분석 사진",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            isLoading -> {
                CircularProgressIndicator(
                    color = Color(0xFF98DB82)
                )
            }

            else -> {
                PlusIcon()
            }
        }
    }
}

@Composable
private fun PlusIcon() {
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

@Composable
private fun GreenBottomButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF98DB82),
            disabledContainerColor = Color(0xFFD9D9D9),
            contentColor = Color.Black,
            disabledContentColor = Color(0xFF777777)
        )
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}