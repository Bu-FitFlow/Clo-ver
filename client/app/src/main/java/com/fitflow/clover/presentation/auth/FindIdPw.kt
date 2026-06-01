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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable

@Preview(
    name = "Pixel 10 - 아이디 비밀번호 찾기",
    device = "spec:width=1080px,height=2424px,dpi=420",
    showBackground = true
)
@Composable
fun FindIdPwPixel10Preview() {
    val fakeNavController = rememberNavController()
    FindIdPw(navController = fakeNavController)
}
//==================================================================================================

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

    val isIdFindEnabled = nameInput.isNotBlank() && emailInput.isNotBlank() && authCodeInput.isNotBlank() && isIdAuthSent
    val isPwFindEnabled = idInput.isNotBlank() && nameInput.isNotBlank() && emailInput.isNotBlank() && authCodeInput.isNotBlank() && isPwAuthSent
    val isPasswordMatching = newPwInput.isNotEmpty() && newPwConfirmInput.isNotEmpty() && (newPwInput == newPwConfirmInput)

    // 🌟 1. 대화면 기기 대응: 전체 화면에서 컨텐츠가 가운데 오도록 래핑
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {

        // 🌟 2. 피그마 기본 디자인 가로 스펙(너비 393.dp)을 기준으로 가로 상자를 고정합니다.
        // 이렇게 하면 내부 Box들의 offset x, y 좌표가 원래 주신 스펙과 100% 똑같이 작동하면서도 전체가 중앙 정렬됩니다.
        Box(
            modifier = Modifier
                .width(393.dp)
                .height(800.dp) // 기본 스크롤 영역 확보
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

            // 🎯 공통 상단 영역: 타이틀 메세지 (Y:71) - 상단 글씨 가운데 정렬 반영
            // (뒤로가기 버튼 위치 보정 및 시안처럼 완전히 화면 중앙에 오도록 x축을 0.dp로 잡고 fillMaxWidth 중앙 정렬)
            Box(
                modifier = Modifier
                    .offset(y = 71.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showPwReset) "비밀번호 찾기" else if (tabIndex == 0) "아이디 찾기" else "비밀번호 찾기",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

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
                        onClick = {
                            tabIndex = 0
                            authCodeInput = ""
                        },
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
                        onClick = {
                            tabIndex = 1
                            authCodeInput = ""
                        },
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
                    // 🍏 [아이디 찾기 입력 란] 원래 주신 좌표값 100% 동일하게 복구
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

                    // 하단 확인 버튼 (X:24, Y:540.19)
                    Button(
                        onClick = { if (isIdFindEnabled) showIdResult = true },
                        enabled = isIdFindEnabled,
                        modifier = Modifier.offset(x = 24.dp, y = 540.19.dp).width(345.dp).height(62.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cloverGreen, disabledContainerColor = Color(0xFFC8E6C9)),
                        shape = RoundedCornerShape(5.dp),
                        elevation = null
                    ) {
                        Text(text = "확인", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                    }

                } else {
                    // 🍎 [비밀번호 찾기 입력 란] 원래 주신 좌표값 100% 동일하게 복구
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

                    // 하단 확인 버튼 (X:24, Y:575)
                    Button(
                        onClick = { if (isPwFindEnabled) showPwReset = true },
                        enabled = isPwFindEnabled,
                        modifier = Modifier.offset(x = 24.dp, y = 575.dp).width(345.dp).height(62.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cloverGreen, disabledContainerColor = Color(0xFFC8E6C9)),
                        shape = RoundedCornerShape(5.dp),
                        elevation = null
                    ) {
                        Text(text = "확인", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // -----------------------------------------------------------------------------------------
            // 🎯 [화면 2] 아이디 찾기 완료 결과 화면
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
                        .clickable {
                            showIdResult = false
                            tabIndex = 1
                            authCodeInput = ""
                        }
                )

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
            // 🎯 [화면 3] 비밀번호 찾기 완료 -> 새 비밀번호 입력 화면
            // -----------------------------------------------------------------------------------------
            if (showPwReset && !showIdResult) {
                Box(modifier = Modifier.offset(x = 43.dp, y = 322.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = newPwInput, onValueChange = { newPwInput = it }, label = "새 password", modifier = Modifier.fillMaxSize())
                }

                Box(modifier = Modifier.offset(x = 43.dp, y = 395.dp).width(309.dp).height(40.dp)) {
                    CloverTextField(value = newPwConfirmInput, onValueChange = { newPwConfirmInput = it }, label = "새 password 재입력", modifier = Modifier.fillMaxSize())
                }

                if (newPwInput.isNotEmpty() && newPwConfirmInput.isNotEmpty()) {
                    Text(
                        text = if (isPasswordMatching) "비밀번호가 일치합니다." else "비밀번호가 일치하지 않습니다.",
                        color = if (isPasswordMatching) Color(0xFF4CAF50) else Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.offset(x = 45.dp, y = 442.dp)
                    )
                }

                Text(
                    text = "아이디를 잊으셨나요?",
                    color = Color.Red,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .offset(x = 129.dp, y = 468.dp)
                        .clickable {
                            showPwReset = false
                            tabIndex = 0
                            isIdAuthSent = false
                            isPwAuthSent = false
                            authCodeInput = ""
                            newPwInput = ""
                            newPwConfirmInput = ""
                        }
                )

                Button(
                    onClick = {
                        if (isPasswordMatching) {
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    enabled = isPasswordMatching,
                    modifier = Modifier.offset(x = 22.dp, y = 515.dp).width(345.dp).height(62.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cloverGreen, disabledContainerColor = Color(0xFFC8E6C9)),
                    shape = RoundedCornerShape(5.dp),
                    elevation = null
                ) {
                    Text(
                        text = "로그인으로",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}