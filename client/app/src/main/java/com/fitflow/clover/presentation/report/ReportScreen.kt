package com.fitflow.clover.presentation.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.R

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
                title = { Text("", fontSize = 18.sp) }, // 타이틀은 비워두거나 '신고' 입력
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back), // 뒤로가기 아이콘
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

            // 헤더 텍스트
            Text(
                text = "신고하는 이유를 선택해주세요",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 신고 사유 리스트
            LazyColumn {
                items(reasons) { reason ->
                    ReportReasonItem(reason = reason) {
                        // 사유 클릭 시 상세 신고 내용 입력 화면 등으로 이동 로직
                        // navController.navigate("report_detail/$reason")
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
        // 구분선
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
    }
}