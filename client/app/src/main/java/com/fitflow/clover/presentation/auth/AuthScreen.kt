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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.R
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.core.component.CloverTextField
import com.fitflow.clover.core.component.CustomCheckBoxRow

// 🎯 1. 로그인 화면 (아이폰 16 비율 스나이핑 + 피그마 실측 디자인 반영)
@Composable
fun LoginMain(navController: NavController) {
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // 피그마와 일치하는 깨끗한 화이트 배경
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp) // 피그마 가로폭 345 맞춤 (393 - 24*2 = 345)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎯 [비율 여백 1] 상단에서 로고 시작점까지의 황금 비율 여백 (Y: 139)
            Spacer(modifier = Modifier.weight(13.9f))

            // 🎯 피그마 실측 로고 크기 반영 (287 * 287)
            // 컴포넌트 내부에 글자가 잘리지 않게 가로세로 비율을 완벽 고정합니다.
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = "Clover Main Logo",
                modifier = Modifier.size(287.dp)
            )

            // 🎯 [비율 여백 2] 로고 하단에서 ID 라벨까지의 여백
            Spacer(modifier = Modifier.weight(3.9f))

            // ID 라벨 및 입력란 (모서리 5, 높이 42)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ID",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal, // Regular
                    color = Color.Black, // 요구사항 반영: 글씨 색상 검은색
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                )
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp), // 피그마 높이 42 규격
                    shape = RoundedCornerShape(5.dp), // 피그마 곡률 5 규격
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }

            // 🎯 [비율 여백 3] ID 입력창과 Password 입력창 사이의 여백
            Spacer(modifier = Modifier.weight(1.8f))

            // Password 라벨 및 입력란 (모서리 5, 높이 42)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal, // Regular
                    color = Color.Black, // 요구사항 반영: 글씨 색상 검은색
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                )
                OutlinedTextField(
                    value = pw,
                    onValueChange = { pw = it },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp), // 피그마 높이 42 규격
                    shape = RoundedCornerShape(5.dp), // 피그마 곡률 5 규격
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }

            // 🎯 [비율 여백 4] 비밀번호 영역과 로그인 버튼 사이의 여백
            Spacer(modifier = Modifier.weight(4.3f))

            // 로그인 버튼 (외곽선 X, 모서리 5, 크기 345*52, 텍스트 Medium 24sp)
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp), // 피그마 규격 높이 52 적용
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                shape = RoundedCornerShape(5.dp),
                elevation = null // 피그마와 동일하게 테두리 외각선 및 그림자 플랫화
            ) {
                Text(
                    text = "로그인",
                    color = Color.Black, // 요구사항 반영: 로그인 글씨 검은색으로 변경!
                    fontSize = 24.sp, // 피그마 크기 24 적용
                    fontWeight = FontWeight.Medium // Medium 적용
                )
            }

            // 🎯 [비율 여백 5] 로그인 버튼에서 하단 가이드 문구까지의 여백
            Spacer(modifier = Modifier.weight(2.2f))

            // 하단 링크 메시지 버튼 영역 (SpaceBetween으로 화면 가로 비율에 맞게 양끝 정렬)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "아이디/비밀번호 찾기",
                    modifier = Modifier.clickable { navController.navigate("find_id_pw") },
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "아직 회원이 아니신가요?",
                    modifier = Modifier.clickable { navController.navigate("join_terms") },
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End
                )
            }

            // 🎯 최하단 컴포넌트 안착을 위한 바닥 가중치 밸런싱 여백 (Y: 624 이하 영역 제어)
            Spacer(modifier = Modifier.weight(22.8f))

            // 키보드 대응 마진 안전장치
            Spacer(modifier = Modifier.height(16.dp))
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

    var isIdChecked by remember { mutableStateOf(false) }
    var isIdAvailable by remember { mutableStateOf(false) }
    var isEmailDuplicate by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp) // 🎯 [핵심 1] 약관 동의 화면과 동일한 좌우 패딩 24.dp 부여!
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎯 로고 위치 가이드 (Y:91)
            Spacer(modifier = Modifier.height(91.dp))
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = null,
                modifier = Modifier.size(200.dp) // 크기 200*200
            )

            // 이름 입력칸 시작점 (Y:290) 보정 마진
            Spacer(modifier = Modifier.height(30.dp))

            // 🎯 피그마 규격 가로폭인 309.dp 영역 잠금 기둥
            Column(
                modifier = Modifier.width(309.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 이름 입력칸: 크기(309*40)
                CloverTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "이름",
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.height(15.dp)) // 간격 15 고정

                // ID 라인 행 구성
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CloverTextField(
                        value = id,
                        onValueChange = {
                            id = it
                            isIdChecked = false
                            isIdAvailable = false
                        },
                        label = "ID",
                        modifier = Modifier
                            .weight(1f) // 가로 242.dp 균형 안착
                            .height(40.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp)) // 버튼과의 정밀 간격

                    if (isIdChecked && isIdAvailable) {
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "사용 가능",
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        // 중복확인 버튼: 크기(60*40), 글씨(Medium, 크기 12), 모서리(5), 외곽선(X)
                        Button(
                            onClick = {
                                isIdChecked = true
                                isIdAvailable = id.isNotBlank() && id != "test"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isIdChecked && !isIdAvailable) Color(0xFFFF6B6B) else Color(0xFF99DE81)
                            ),
                            shape = RoundedCornerShape(5.dp),
                            elevation = null,
                            modifier = Modifier.width(60.dp).height(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (isIdChecked && !isIdAvailable) "불가" else "중복확인",
                                color = if (isIdChecked && !isIdAvailable) Color.White else Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if (isIdChecked && !isIdAvailable) {
                    Text(
                        text = "이미 사용 중이거나 유효하지 않은 아이디입니다.",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // password 입력칸: 크기(309*40)
                CloverTextField(value = pw, onValueChange = { pw = it }, label = "password", modifier = Modifier.height(40.dp))

                Spacer(modifier = Modifier.height(15.dp))

                // password 재입력: 크기(309*40)
                CloverTextField(value = pwConfirm, onValueChange = { pwConfirm = it }, label = "password 재입력", modifier = Modifier.height(40.dp))

                Spacer(modifier = Modifier.height(15.dp))

                // 이메일 입력칸: 크기(309*40)
                CloverTextField(
                    value = email,
                    onValueChange = { email = it; isEmailDuplicate = (it == "test") },
                    label = "이메일",
                    modifier = Modifier.height(40.dp)
                )

                if (isEmailDuplicate) {
                    Text(
                        text = "중복 된 이메일 입니다.",
                        color = Color.Red,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // 닉네임 입력칸: 크기(309*40)
                CloverTextField(value = nickname, onValueChange = { nickname = it }, label = "닉네임", modifier = Modifier.height(40.dp))

                Spacer(modifier = Modifier.height(45.dp))

                // 이미 계정이 있으신가요? (검은색, 폰트 regular, 크기 16)
                Text(
                    text = "이미 계정이 있으신가요?",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable {
                        navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    }
                )
            }

            // 🎯 [핵심 2] 약관 동의 화면의 마진 메커니즘 일치화
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 하단 완료 버튼: 약관동의 화면의 다음 버튼과 완벽히 동일한 위치 및 크기 메커니즘
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth() // 💡 상단에서 지정한 24.dp 패딩과 맞물려 완벽하게 345.dp 크기 및 정렬 만족!
                    .height(63.dp), // 높이 63 똑같이 세팅
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99DE81)),
                shape = RoundedCornerShape(5.dp),
                elevation = null
            ) {
                Text(
                    text = "완료",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }

            // 🎯 약관 동의 화면의 최하단 여백과 동일하게 일치화
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
// 🎯 3. 약관 동의 화면 (기능/라우팅 원본 유지 + UI 디자인 스펙만 정밀 반영)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTerms(navController: NavController) {
    var term1 by remember { mutableStateOf(false) }
    var term2 by remember { mutableStateOf(false) }
    var term3 by remember { mutableStateOf(false) }
    val isAllChecked = term1 && term2 && term3

    Scaffold(
        containerColor = Color.White, // 피그마 기준 깨끗한 화이트 배경
        topBar = {
            // 🎯 상단 커스텀 영역 (Y: 63 눈눈높이 타겟팅)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 15.dp) // 피그마 Y:63 라인을 맞추기 위한 상단 마진
                    .height(48.dp)
            ) {
                // 🎯 뒤로가기 아이콘 (크기 40*40, 위치 X:27)
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = 27.dp) // 요청 스펙 X:27 반영
                        .size(40.dp) // 요청 스펙 크기 40*40 반영
                        .align(Alignment.CenterStart)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 🎯 상단 메시지 (폰트 Medium, 크기 28, 색상 검은색, 위치 X:149, Y:71.73)
                Text(
                    text = "약관 동의",
                    fontSize = 28.sp, // 요청 스펙 크기 28 반영
                    fontWeight = FontWeight.Medium, // Medium 적용
                    color = Color.Black, // 검은색 적용
                    modifier = Modifier
                        .padding(start = 149.dp) // 요청 스펙 X:149 반영
                        .align(Alignment.CenterStart)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(8.5f))

            // 중앙 로고 이미지 (180*180)
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "회원님 환영합니다!",
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(6f))

            // ⭐️ 원래 작성하셨던 오리지널 기능 코드를 그대로 유지합니다. (절대 안 튕김)
            CustomCheckBoxRow("약관 전체 동의", isAllChecked, { val t = !isAllChecked; term1 = t; term2 = t; term3 = t })
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray)

            CustomCheckBoxRow("이용약관 동의(필수)", term1, { term1 = it }, { navController.navigate("term_detail_1") })
            CustomCheckBoxRow("개인정보 수집 및 이용동의(필수)", term2, { term2 = it }, { navController.navigate("term_detail_2") })
            CustomCheckBoxRow("가입 시 알림 동의(선택)", term3, { term3 = it })

            Spacer(modifier = Modifier.weight(15f))

            // 🎯 하단 다음 버튼 (크기 345*63, 위치 X:24 배치, 색상 99DE81, 모서리 5, 외곽선 X)
            Button(
                onClick = { navController.navigate("join_detail") },
                enabled = term1 && term2,
                modifier = Modifier
                    .fillMaxWidth() // 가로 패딩 24.dp와 결합되어 자동으로 가로폭 345.dp 규격 충족
                    .height(63.dp), // 요청 스펙 높이 63 반영
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF99DE81), // 색상 99DE81 반영
                    disabledContainerColor = Color(0xFFC8E6C9)
                ),
                shape = RoundedCornerShape(5.dp), // 모서리 5 반영
                elevation = null // 외곽선 X, 그림자 제거 플랫화
            ) {
                // 🎯 다음 버튼 글씨 (폰트 Medium, 크기 24, 색상 검은색)
                Text(
                    text = "다음",
                    color = Color.Black, // 검은색 적용
                    fontWeight = FontWeight.Medium, // Medium 적용
                    fontSize = 24.sp // 크기 24 적용
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}