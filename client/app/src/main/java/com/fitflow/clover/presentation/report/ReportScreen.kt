package com.fitflow.clover.presentation.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fitflow.clover.R
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.core.theme.CloverLightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current

    // 뷰모델 상태 관찰 수집 (StateFlow 연동)
    val selectedReason by viewModel.selectedReason.collectAsState()
    val reportContent by viewModel.reportContent.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val temporarySelectedReason by viewModel.temporarySelectedReason.collectAsState()

    // 1단계 로직: 확정된 유형과 입력된 내용이 모두 채워져야만 제출하기 활성화
    val isSubmitEnabled = viewModel.checkSubmitEnabled(selectedReason, reportContent)

    Scaffold(
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("신고하기", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        // 뒤로가기 아이콘 클릭 시 (back 리소스 사용)
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.back),
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("신고 대상", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Spacer(modifier = Modifier.height(24.dp))
            Text("신고 유형", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            // ① 신고 유형 셀렉터 영역 (클릭 시 2번째 바텀시트 노출)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFFAFAFA), shape = RoundedCornerShape(8.dp))
                    .clickable { viewModel.setShowBottomSheet(true) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    // 3단계 로직: 선택된 유형이 확정되면 텍스트 반영
                    text = selectedReason.ifEmpty { "신고 유형을 선택해주세요." },
                    color = if (selectedReason.isEmpty()) Color.LightGray else Color.Black,
                    fontSize = 14.sp
                )
                Icon(
                    // Chevron_Left 아이콘을 아래 방향(🔽) 모양으로 회전 매핑
                    imageVector = ImageVector.vectorResource(id = R.drawable.chevron_left),
                    contentDescription = "열기",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp).rotate(270f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("신고 내용", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            // ② 본문 텍스트 내용 입력창 (버전 호환성 에러 완벽 해결 반영)
            OutlinedTextField(
                value = reportContent,
                onValueChange = { viewModel.setReportContent(it) },
                placeholder = {
                    Text("신고 내용을 입력해주세요.", color = Color.LightGray, fontSize = 14.sp)
                },
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        // 🎯 숫자를 직접 쓰는 대신 Color.kt에 등록한 이름표를 넣어줍니다!
                        focusedBorderColor = CloverLightGray,
                        unfocusedBorderColor = CloverLightGray,

                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )


            Spacer(modifier = Modifier.weight(1f))

            // ③ 최하단 제출하기 버튼
            Button(
                onClick = {
                    if (isSubmitEnabled) {
                        Toast.makeText(context, "신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                enabled = isSubmitEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CloverGreen,
                    disabledContainerColor = Color(0xFFD3D3D3)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "제출하기",
                    color = if (isSubmitEnabled) Color.Black else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // -----------------------------------------------------------------
    // ② 2번째 페이지 바텀시트 구현 (라디오 버튼 파란색 쌍 동그라미)
    // -----------------------------------------------------------------
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowBottomSheet(false) },
            containerColor = Color.White,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "신고 유형을 선택해주세요.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = { viewModel.setShowBottomSheet(false) }) {
                        // 우측 상단 닫기 아이콘 (x 리소스 사용)
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.x),
                            contentDescription = "닫기",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 뷰모델에 정의된 단일 출처 리스트 사용
                viewModel.reportReasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setTemporaryReason(reason) } // 글씨 클릭 연동
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (temporarySelectedReason == reason),
                            onClick = { viewModel.setTemporaryReason(reason) }, // 동그라미 클릭 연동
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF1E90FF), // 시안의 파란색 동그라미
                                unselectedColor = Color.LightGray
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = reason, fontSize = 15.sp, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 바텀시트 하단 '다음' 버튼
                Button(
                    onClick = {
                        // 🎯 임시 저장된 값을 메인으로 확정하고 바텀시트를 닫는 뷰모델 연동 완료!
                        viewModel.confirmReason()
                    },
                    enabled = temporarySelectedReason.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CloverGreen,
                        disabledContainerColor = Color(0xFFD3D3D3)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "다음",
                        color = if (temporarySelectedReason.isNotEmpty()) Color.Black else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}