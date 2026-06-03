package com.fitflow.clover.presentation.report

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fitflow.clover.R

// -----------------------------------------------------------------
// 🎯 메인 신고하기 화면 (ReportScreen) - 피그마 픽셀 매칭 가이드 버젼
// -----------------------------------------------------------------
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = viewModel(),
    targetUserName: String = "신고대상"
) {
    val context = LocalContext.current

    val selectedReason by viewModel.selectedReason.collectAsState()
    val reportContent by viewModel.reportContent.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()

    val isSubmitEnabled = viewModel.checkSubmitEnabled(selectedReason, reportContent)

    val cloverGreen = Color(0xFF99DE81)   // 피그마 테마 초록색

    var tempSelectedReason by remember { mutableStateOf("") }

    // 🎯 모달창 내 '다음' 버튼 활성화 여부 판별 (공백이 아닐 때만 true)
    val isNextEnabled = tempSelectedReason.isNotBlank()

    val reasonList = listOf(
        "사기 피해를 입었어요.",
        "욕설, 비방, 혐오적인 표현을 해요.",
        "물품 하자, 구매 미확정이 발생했어요.",
        "기타"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(786.dp)) {

                // 뒤로가기 버튼: 크기(40*40), 위치(X:27, Y:63)
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .offset(x = 27.dp, y = 63.dp)
                        .size(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 상단 타이틀: 크기(28sp), 위치(X:149, Y:71.73)
                Text(
                    text = "신고하기",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.offset(x = 149.dp, y = 71.73.dp)
                )

                // 신고 대상 라벨: 크기(20sp), 위치(X:16, Y:123)
                Text(
                    text = "신고 대상 : $targetUserName",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.offset(x = 16.dp, y = 123.dp)
                )

                // 신고 유형 타이틀: 크기(16sp), 위치(X:16, Y:163)
                Text(
                    text = "신고 유형",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.offset(x = 16.dp, y = 163.dp)
                )

                // ① 신고 유형 셀렉터 박스: 크기(363*54), 위치(X:16, Y:193)
                Row(
                    modifier = Modifier
                        .offset(x = 16.dp, y = 193.dp)
                        .size(width = 363.dp, height = 54.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                        .clickable {
                            tempSelectedReason = selectedReason
                            viewModel.setShowBottomSheet(true)
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedReason.ifEmpty { "신고 유형을 선택해주세요." },
                        color = if (selectedReason.isEmpty()) Color.Black.copy(alpha = 0.4f) else Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = "드롭다운 화살표",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(-90f)
                    )
                }

                // 신고 내용 타이틀: 크기(16sp), 위치(X:16, Y:269)
                Text(
                    text = "신고 내용",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.offset(x = 16.dp, y = 269.dp)
                )

                // ② 본문 텍스트 입력 영역: 크기(363*180), 위치(X:16, Y:300)
                Box(
                    modifier = Modifier
                        .offset(x = 16.dp, y = 300.dp)
                        .size(width = 363.dp, height = 180.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    BasicTextField(
                        value = reportContent,
                        onValueChange = { viewModel.setReportContent(it) },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxSize(),
                        decorationBox = { innerTextField ->
                            if (reportContent.isEmpty()) {
                                Text(
                                    text = "신고 내용을 입력해주세요.",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // ③ 최하단 제출하기 가로 풀 버튼: 크기(393*67), 위치(X:2, Y:786)
            Button(
                onClick = {
                    if (isSubmitEnabled) {
                        Toast.makeText(context, "신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                enabled = isSubmitEnabled,
                modifier = Modifier
                    .offset(x = 2.dp, y = 0.dp)
                    .size(width = 393.dp, height = 67.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cloverGreen,
                    disabledContainerColor = Color(0xFFC8E6C9)
                ),
                shape = RoundedCornerShape(0.dp),
                elevation = null
            ) {
                Text(
                    text = "제출하기",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // -----------------------------------------------------------------------------------------
        // 🎯 하단 유형 선택 팝업 레이아웃 모달창 영역
        // -----------------------------------------------------------------------------------------
        if (showBottomSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { viewModel.setShowBottomSheet(false) }
            ) {
                Column(
                    modifier = Modifier
                        .offset(x = 2.dp, y = 521.dp)
                        .size(width = 393.dp, height = 332.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                        )
                        .clickable(enabled = false) { }
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(65.dp)) {
                        Text(
                            text = "신고 유형을 선택해주세요.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.offset(x = 22.dp, y = 30.dp)
                        )

                        IconButton(
                            onClick = { viewModel.setShowBottomSheet(false) },
                            modifier = Modifier
                                .offset(x = 350.dp, y = 22.dp)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.x),
                                contentDescription = "팝업 닫기",
                                tint = Color.Black,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // 라디오 유형 선택 본문 반복 리스트
                    reasonList.forEachIndexed { index, reason ->
                        val topSpace = when (index) {
                            0 -> 13.dp
                            1 -> 20.dp
                            2 -> 21.dp
                            else -> 21.dp
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, top = topSpace)
                                .selectable(
                                    selected = (tempSelectedReason == reason),
                                    onClick = { tempSelectedReason = reason }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (tempSelectedReason == reason),
                                onClick = { tempSelectedReason = reason },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = cloverGreen,
                                    unselectedColor = Color.LightGray
                                ),
                                modifier = Modifier.size(20.dp)
                            )

                            Text(
                                text = reason,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 14.dp)
                            )
                        }
                    }

                    // 🛠️ 조건부 활성화 적용 완료: 선택했을 때만 색상이 활성화되고 클릭이 가능해집니다.
                    Button(
                        onClick = {
                            if (isNextEnabled) {
                                //viewModel.setSelectedReason(tempSelectedReason)
                                viewModel.setShowBottomSheet(false)
                            }
                        },
                        enabled = isNextEnabled,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 22.dp)
                            .size(width = 345.dp, height = 62.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cloverGreen,
                            disabledContainerColor = Color(0xFFC8E6C9)
                        ),
                        shape = RoundedCornerShape(5.dp),
                        elevation = null
                    ) {
                        Text(
                            text = "다음",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

