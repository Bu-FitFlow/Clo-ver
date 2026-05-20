package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.R
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.core.component.CloverTextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.core.content.pm.ShortcutInfoCompat
import com.fitflow.clover.core.component.CustomCheckBoxRow
import androidx.compose.foundation.layout.navigationBarsPadding

// 1. 로그인 화면 (기존 유지)
@Composable
fun LoginMain(navController: NavController) {
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.padding(horizontal = 30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.frame_31), contentDescription = null, modifier = Modifier.size(250.dp))
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = pw, onValueChange = { pw = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(62.dp), colors = ButtonDefaults.buttonColors(containerColor = com.fitflow.clover.core.theme.CloverGreen), shape = RoundedCornerShape(5.dp)) { Text("로그인", color = Color.Black) }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Text("아이디/비밀번호 찾기", modifier = Modifier.clickable { navController.navigate("find_id_pw") }, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(100.dp))
                Text("아직 회원이 아니신가요?", modifier = Modifier.clickable { navController.navigate("join_terms") }, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinDetail(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var pwConfirm by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    var idCheckStatus by remember { mutableIntStateOf(0) }
    var isEmailDuplicate by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // 하단에 고정된 완료 버튼
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    // ⭐ [핵심 추가] 시스템 네비게이션 바(홈/뒤로가기) 두께만큼 하단 패딩을 자동으로 부여합니다.
                    .navigationBarsPadding(),
                shadowElevation = 8.dp
            ) {
                // 패딩 공간과 분리하기 위해 겉을 조금 더 깔끔하게 감싸줍니다.
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 20.dp, vertical = 4.dp), // 버튼 좌우 여백 및 아래 미세 정렬
                    colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Text("완료", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Scaffold가 계산해 준 패딩(bottomBar의 높이 포함)을 적용
                .padding(horizontal = 35.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            CloverTextField(value = name, onValueChange = { name = it }, label = "이름")
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                CloverTextField(
                    value = id,
                    onValueChange = { id = it; idCheckStatus = 0 },
                    label = "ID",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))

                when (idCheckStatus) {
                    1 -> Image(painter = painterResource(id = R.drawable.check), contentDescription = null, modifier = Modifier.size(24.dp))
                    2 -> Button(
                        onClick = { idCheckStatus = 1 },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(40.dp)
                    ) { Text("중복", color = Color.White, fontSize = 12.sp) }
                    else -> Button(
                        onClick = { idCheckStatus = 2 },
                        colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(40.dp)
                    ) { Text("중복 확인", color = Color.Black, fontSize = 11.sp) }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            CloverTextField(value = pw, onValueChange = { pw = it }, label = "password")
            Spacer(modifier = Modifier.height(8.dp))
            CloverTextField(value = pwConfirm, onValueChange = { pwConfirm = it }, label = "password 재입력")
            Spacer(modifier = Modifier.height(8.dp))

            CloverTextField(
                value = email,
                onValueChange = { email = it; isEmailDuplicate = (it == "test") },
                label = "이메일"
            )
            if (isEmailDuplicate) {
                Text("중복 된 이메일 입니다.", color = Color.Red, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            CloverTextField(value = nickname, onValueChange = { nickname = it }, label = "닉네임")

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("이미 계정이 있으신가요?", fontSize = 14.sp, color = Color.DarkGray)
                Text(
                    text = "로그인",
                    fontSize = 14.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp).clickable {
                        navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    }
                )
            }

            // 스크롤 영역 최하단 여유 마진
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTerms(navController: NavController) {
    var term1 by remember { mutableStateOf(false) }
    var term2 by remember { mutableStateOf(false) }
    var term3 by remember { mutableStateOf(false) }
    val isAllChecked = term1 && term2 && term3

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text("약관 동의", fontSize = 28.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(vertical = 0.dp, horizontal = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.frame_31), contentDescription = null, modifier = Modifier.size(200.dp))
                Text("회원님 환영합니다!", fontWeight = FontWeight.Bold, fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))
            CustomCheckBoxRow("약관 전체 동의", isAllChecked, { val t = !isAllChecked; term1 = t; term2 = t; term3 = t })
            HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp), thickness = 1.dp)
            CustomCheckBoxRow("이용약관 동의(필수)", term1, { term1 = it }, { navController.navigate("term_detail_1") })
            CustomCheckBoxRow("개인정보 수집 및 이용동의(필수)", term2, { term2 = it }, { navController.navigate("term_detail_2") })
            CustomCheckBoxRow("가입 시 알림 동의(선택)", term3, { term3 = it })
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("join_detail") },
                modifier = Modifier.fillMaxWidth().height(55.dp).padding(bottom = 10.dp),
                enabled = term1 && term2,
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen, disabledContainerColor = Color(0xFFC8E6C9)),
                shape = RoundedCornerShape(8.dp)
            ) { Text("다음", color = Color.Black, fontWeight = FontWeight.Bold) }
        }
    }
}

