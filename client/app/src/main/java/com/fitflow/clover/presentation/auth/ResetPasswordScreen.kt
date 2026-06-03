package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.core.component.CustomOutlinedInput
import com.fitflow.clover.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Devices
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var newPw by remember { mutableStateOf("") }
    var confirmPw by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text("비밀번호 찾기", fontSize = 22.sp, fontWeight = FontWeight.Bold) }, // 피그마 타이틀 텍스트 일치
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 34.dp) // 최신 안드로이드 제스처 바 공간 확보 여백
            ) {
                Button(
                    onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.2f)),
                    elevation = null
                ) {
                    Text("로그인으로", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        // 입력 박스들과 링크 영역은 기기 크기에 맞춰 스크롤 및 중앙 정렬 유지
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // 작은 기기에서 화면이 터지는 것을 방지
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // 피그마 상단 컴포넌트 여백 밸런싱

            CustomOutlinedInput(
                value = newPw,
                onValueChange = { newPw = it },
                placeholder = "새 password"
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomOutlinedInput(
                value = confirmPw,
                onValueChange = { confirmPw = it },
                placeholder = "새 password 재입력"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 아이디 찾기 안내 문구
            Text(
                text = "아이디를 잊으셨나요?",
                color = Color(0xFFFF5A5A), // 지나치게 원색인 Red 대신 피그마의 소프트 레드 색감 필터링
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { navController.navigate("find_id_pw") }
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}