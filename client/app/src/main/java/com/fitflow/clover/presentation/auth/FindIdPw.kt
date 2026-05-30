package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.core.component.CloverTextField
import com.fitflow.clover.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindIdPw(navController: NavController) {
    var tabIndex by remember { mutableIntStateOf(0) } // 0: 아이디 찾기, 1: 비밀번호 찾기
    var idInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var authCodeInput by remember { mutableStateOf("") }
    var newPwInput by remember { mutableStateOf("") }
    var newPwConfirmInput by remember { mutableStateOf("") }

    var isIdAuthSent by remember { mutableStateOf(false) }
    var isPwAuthSent by remember { mutableStateOf(false) }

    var showIdResult by remember { mutableStateOf(false) }
    var showPwReset by remember { mutableStateOf(false) }

    val cloverGreen = Color(0xFF99DE81)

    // 불필요한 BoxWithConstraints 대신 일반 Box로 깔끔하게 래핑
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 🎯 공통 상단 영역: 뒤로가기 아이콘 (X:27, Y:63)
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .offset(x = 27.dp, y = 63.dp)
                .size(40.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 🎯 공통 상단 영역: 타이틀 메세지 (Y:71)
        Text(
            text = if (showPwReset) "비밀번호 찾기" else if (tabIndex == 0) "아이디 찾기" else "비밀번호 찾기",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.offset(
                x = if (tabIndex == 0 && !showPwReset) 132.dp else 121.dp,
                y = 71.dp
            )
        )

        // -----------------------------------------------------------------------------------------
        // 🎯 [화면 1] 메인 입력 폼 (아이디 찾기 / 비밀번호 찾기 기본 입력)
        // -----------------------------------------------------------------------------------------
        if (!showIdResult && !showPwReset) {

            // 상단 탭 그룹 버튼 (X:42, Y:135.83)
            Row(
                modifier = Modifier
                    .offset(x = 42.dp, y = 135.83.dp)
                    .width(309.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { tabIndex = 0 },
                    modifier = Modifier.width(150.dp).height(40.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tabIndex == 0) cloverGreen.copy(alpha = 0.3f) else Color.White
                    ),
                    border = if (tabIndex == 0) null else BorderStroke(1.dp, Color.Black),
                    elevation = null,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("아이디 찾기", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }

                Button(
                    onClick = { tabIndex = 1 },
                    modifier = Modifier.width(150.dp).height(40.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tabIndex == 1) cloverGreen.copy(alpha = 0.3f) else Color.White
                    ),
                    border = if (tabIndex == 1) null else BorderStroke(1.dp, Color.Black),
                    elevation = null,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("비밀번호 찾기", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }
            }

            if (tabIndex == 0) {
                // 🍏 [아이디 찾기 입력 란]
                Box(modifier = Modifier.offset(x = 42.dp, y = 300.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = nameInput, onValueChange = { nameInput = it }, label = "이름 입력", modifier = Modifier.fillMaxSize())
                }
                Box(modifier = Modifier.offset(x = 42.dp, y = 375.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = emailInput, onValueChange = { emailInput = it }, label = "이메일 입력", modifier = Modifier.fillMaxSize())
                }
                Box(modifier = Modifier.offset(x = 42.dp, y = 448.46.dp).width(235.dp).height(40.dp)) {
                    CloverTextField(value = authCodeInput, onValueChange = { authCodeInput = it }, label = "인증번호 6자리", modifier = Modifier.fillMaxSize())
                }
                Button(
                    onClick = { isIdAuthSent = true },
                    modifier = Modifier.offset(x = 290.88.dp, y = 448.46.dp).width(60.dp).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cloverGreen),
                    shape = RoundedCornerShape(5.dp),
                    elevation = null,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = if (isIdAuthSent) "재인증" else "인증받기", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                if (isIdAuthSent) {
                    Text(
                        text = "인증번호가 오지 않았습니까?",
                        color = Color.Red,
                        fontSize = 15.sp,
                        modifier = Modifier.offset(x = 43.dp, y = 489.dp)
                    )
                }

                // 하단 확인 버튼 (Y: 540.19)
                Button(
                    onClick = { showIdResult = true },
                    modifier = Modifier.offset(x = 24.dp, y = 540.19.dp).width(345.dp).height(62.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cloverGreen),
                    shape = RoundedCornerShape(5.dp),
                    elevation = null
                ) {
                    Text("확인", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                }

            } else {
                // 🍎 [비밀번호 찾기 입력 란]
                Box(modifier = Modifier.offset(x = 43.dp, y = 260.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = idInput, onValueChange = { idInput = it }, label = "ID", modifier = Modifier.fillMaxSize())
                }
                Box(modifier = Modifier.offset(x = 43.dp, y = 330.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = nameInput, onValueChange = { nameInput = it }, label = "이름 입력", modifier = Modifier.fillMaxSize())
                }
                Box(modifier = Modifier.offset(x = 43.dp, y = 400.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = emailInput, onValueChange = { emailInput = it }, label = "이메일", modifier = Modifier.fillMaxSize())
                }
                Box(modifier = Modifier.offset(x = 43.dp, y = 479.dp).width(235.dp).height(40.dp)) {
                    CloverTextField(value = authCodeInput, onValueChange = { authCodeInput = it }, label = "인증번호 6자리", modifier = Modifier.fillMaxSize())
                }
                Button(
                    onClick = { isPwAuthSent = true },
                    modifier = Modifier.offset(x = 292.dp, y = 479.dp).width(60.dp).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cloverGreen),
                    shape = RoundedCornerShape(5.dp),
                    elevation = null,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = if (isPwAuthSent) "재인증" else "인증받기", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                if (isPwAuthSent) {
                    Text(
                        text = "인증번호가 오지 않았습니까?",
                        color = Color.Red,
                        fontSize = 15.sp,
                        modifier = Modifier.offset(x = 43.dp, y = 528.dp)
                    )
                }

                // 하단 확인 버튼 (Y: 575)
                Button(
                    onClick = { showPwReset = true },
                    modifier = Modifier.offset(x = 24.dp, y = 575.dp).width(345.dp).height(62.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cloverGreen),
                    shape = RoundedCornerShape(5.dp),
                    elevation = null
                ) {
                    Text("확인", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // -----------------------------------------------------------------------------------------
        // 🎯 [화면 2] 아이디 찾기 완료 결과 화면 (조건 분리 수정완료)
        // -----------------------------------------------------------------------------------------
        if (showIdResult && !showPwReset) {
            Text(
                text = "ID",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.offset(x = 50.dp, y = 314.dp)
            )

            Box(
                modifier = Modifier
                    .offset(x = 42.dp, y = 344.dp)
                    .width(309.dp)
                    .height(40.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "user_id_example", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            }

            Text(
                text = "비밀번호를 잊으셨나요?",
                color = Color.Red,
                fontSize = 15.sp,
                modifier = Modifier
                    .offset(x = 112.dp, y = 404.dp)
                    .clickable { showIdResult = false; tabIndex = 1 }
            )

            // 로그인으로 버튼 (Y: 491)
            Button(
                onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                modifier = Modifier.offset(x = 28.dp, y = 491.dp).width(345.dp).height(62.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cloverGreen),
                shape = RoundedCornerShape(5.dp),
                elevation = null
            ) {
                Text("로그인으로", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Medium)
            }
        }

        // -----------------------------------------------------------------------------------------
        // 🎯 [화면 3] 비밀번호 찾기 완료 -> 새 비밀번호 입력 화면 (조건 분리 수정완료)
        // -----------------------------------------------------------------------------------------
        if (showPwReset && !showIdResult) {
            Box(modifier = Modifier.offset(x = 43.dp, y = 322.dp).width(309.dp).height(40.dp)) {
                CloverTextField(value = newPwInput, onValueChange = { newPwInput = it }, label = "새 password", modifier = Modifier.fillMaxSize())
            }

            Box(modifier = Modifier.offset(x = 43.dp, y = 395.dp).width(309.dp).height(40.dp)) {
                CloverTextField(value = newPwConfirmInput, onValueChange = { newPwConfirmInput = it }, label = "새 password 재입력", modifier = Modifier.fillMaxSize())
            }

            Text(
                text = "아이디를 잊으셨나요?",
                color = Color.Red,
                fontSize = 15.sp,
                modifier = Modifier
                    .offset(x = 129.dp, y = 444.dp)
                    .clickable {
                        showPwReset = false
                        tabIndex = 0
                        isIdAuthSent = false
                    }
            )

            // 🛠️ 아이디 결과창의 버튼 위치와 100% 동일하게 일치시킨 로그인 버튼 (X:22, Y:491)
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.offset(x = 22.dp, y = 491.dp).width(345.dp).height(62.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cloverGreen),
                shape = RoundedCornerShape(5.dp),
                elevation = null
            ) {
                Text("로그인으로", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}