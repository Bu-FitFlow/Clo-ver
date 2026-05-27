package com.fitflow.clover.presentation.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue // ⭐ by 키워드 위임 에러 방지용 임포트 필수!
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fitflow.clover.R
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.presentation.report.component.ReportReasonBottomSheet

// -----------------------------------------------------------------
// 1. 조원의 이전 페이지 연동용 예비 화면 (ReportReasonScreen)
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportReasonScreen(navController: NavController) {
    val reasons = listOf(
        "사기 피해를 입었어요.",
        "욕설, 비방, 혐오적인 표현을 해요.",
        "물품 하자, 구매 미확정이 발생했어요.",
        "기타"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("신고", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "신고하는 이유를 선택해주세요",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn {
                items(reasons) { reason ->
                    ReportReasonItem(reason = reason) {
                        // 필요한 경우 상세 라우팅 연동 가능
                    }
                }
            }
        }
    }
}

@Composable
fun ReportReasonItem(reason: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = reason,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
    }
}


// -----------------------------------------------------------------
// 2. 메인 신고하기 화면 (ReportScreen) - 에러 수정 완료 버젼
// -----------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current

    // 뷰모델 상태 관찰 수집 (임포트 충돌 및 중복 정의 제거 완료)
    val selectedReason by viewModel.selectedReason.collectAsState()
    val reportContent by viewModel.reportContent.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()

    // 🌟 중복을 없애고 안전한 우회 체크 함수 하나로 통일하여 할당합니다!
    val isSubmitEnabled = viewModel.checkSubmitEnabled(selectedReason, reportContent)

    Scaffold(
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("신고하기", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        // 다른 조원이 만든 이전 화면(상세보기 등)으로 뒤로가기
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.chevron_left),
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

            Spacer(modifier = Modifier.height(16.dp))
            Text("신고 유형", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            // ① 신고 유형 셀렉터 드롭박스 박스 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .clickable { viewModel.setShowBottomSheet(true) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedReason.ifEmpty { "신고 유형을 선택해주세요." },
                    color = if (selectedReason.isEmpty()) Color.LightGray else Color.Black,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.chevron_left),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("신고 내용", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            // ② 본문 텍스트 내용 입력창
            Spacer(modifier = Modifier.height(24.dp))
            Text("신고 내용", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            // ② 본문 텍스트 내용 입력창 (버전 호환성 에러 수정 완료!)
            OutlinedTextField(
                value = reportContent,
                onValueChange = { viewModel.setReportContent(it) },
                placeholder = {
                    Text("신고 내용을 입력해주세요.", color = Color.LightGray, fontSize = 14.sp)
                },
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(8.dp),
                // 🌟 이 부분을 의논하신 대로 버전 호환 이름으로 수정했습니다!
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

    // 바텀 시트 연결
    ReportReasonBottomSheet(
        viewModel = viewModel,
        showBottomSheet = showBottomSheet
    )
}
