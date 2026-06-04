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

@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = viewModel(),
    targetUserName: String = "신고대상"
) {
    val context = LocalContext.current

    // 뷰모델의 상태들을 실시간 관찰 (Compose State로 변환)
    val selectedReason by viewModel.selectedReason.collectAsState()
    val reportContent by viewModel.reportContent.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val temporarySelectedReason by viewModel.temporarySelectedReason.collectAsState() // 🌟 뷰모델의 임시 사유 상태 추가

    // 제출하기 버튼 활성화 여부
    val isSubmitEnabled = viewModel.checkSubmitEnabled(selectedReason, reportContent)

    // 모달창 내부 '다음' 버튼 활성화 여부 (뷰모델의 임시 값이 비어있지 않을 때만 활성화)
    val isNextEnabled = temporarySelectedReason.isNotEmpty()

    val cloverGreen = Color(0xFF99DE81)
    val reasonList = viewModel.reportReasons

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding() // 기기 하단 소프트바 잘림 방지
    ) {
        // [상단 타이틀 & 스크롤 본문 영역]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 67.dp) // 최하단 '제출하기' 버튼 높이만큼 여백 확보
        ) {
            // 1. 커스텀 상단 탑바 (좌우 정렬 균형 보정)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp, bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .size(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(
                    text = "신고하기",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            // 2. 반응형 본문 콘텐츠 영역 (패딩 지정을 통해 왼쪽 쏠림 해결)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // 신고 대상 라벨
                Text(
                    text = "신고 대상 : $targetUserName",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 신고 유형 타이틀
                Text(
                    text = "신고 유형",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ① 신고 유형 셀렉터 박스 (가로 100% 꽉 차게 반응형 설정)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                        .clickable {
                            // 팝업을 열면 뷰모델 함수 내부에서 자동으로 기존 선택값을 임시값에 세팅해줍니다.
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

                Spacer(modifier = Modifier.height(24.dp))

                // 신고 내용 타이틀
                Text(
                    text = "신고 내용",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ② 본문 텍스트 입력 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
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

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // ③ 최하단 고정 제출하기 풀 버튼 (바닥 레이아웃 잘림 방지 완료)
        Button(
            onClick = {
                if (isSubmitEnabled) {
                    Toast.makeText(context, "신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            },
            enabled = isSubmitEnabled,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(67.dp),
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // -----------------------------------------------------------------------------------------
        // 🎯 하단 유형 선택 팝업 레이아웃 모달창 영역 (뷰모델 상태 기반으로 전면 리팩토링)
        // -----------------------------------------------------------------------------------------
        if (showBottomSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { viewModel.setShowBottomSheet(false) }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // 고정 좌표 대신 화면 최하단 정렬
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .padding(bottom = 24.dp)
                        .clickable(enabled = false) { }
                ) {
                    // 모달 팝업 타이틀 헤더
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "신고 유형을 선택해주세요.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )

                        IconButton(
                            onClick = { viewModel.setShowBottomSheet(false) },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
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

                    // 라디오 버튼 반복 생성 리스트 (오직 뷰모델의 temporarySelectedReason만 바라봄)
                    reasonList.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 10.dp)
                                .selectable(
                                    selected = (temporarySelectedReason == reason),
                                    onClick = { viewModel.setTemporaryReason(reason) }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (temporarySelectedReason == reason),
                                onClick = { viewModel.setTemporaryReason(reason) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = cloverGreen,
                                    unselectedColor = Color.LightGray
                                ),
                                modifier = Modifier.size(20.dp)
                            )

                            Text(
                                text = reason,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🛠️ 조원분 뷰모델 설계와 100% 동기화된 확정 버튼 로직
                    Button(
                        onClick = {
                            if (isNextEnabled) {
                                viewModel.confirmReason() // 클릭 시 임시값 -> 확정값 복사 및 팝업 자동으로 닫힘
                            }
                        },
                        enabled = isNextEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cloverGreen,
                            disabledContainerColor = Color(0xFFC8E6C9)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = null
                    ) {
                        Text(
                            text = "다음",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}