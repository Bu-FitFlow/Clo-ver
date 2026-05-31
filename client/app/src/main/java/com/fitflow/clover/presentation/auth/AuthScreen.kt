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
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType

val CloverGreen = Color(0xFF99DE81)

@Composable
fun LoginMain(navController: NavController) {
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎯 [비율 여백 1] 상단 여백
            Spacer(modifier = Modifier.weight(13.9f))

            // 🎯 Clover 메인 로고
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = "Clover Main Logo",
                modifier = Modifier.size(287.dp)
            )

            // 🎯 [비율 여백 2] 로고 하단 여백
            Spacer(modifier = Modifier.weight(3.9f))

            // ID 입력란 영역
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ID",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                )

                // 🛠️ contentPadding 에러 원천 차단: 높이 고정에 최적화된 Custom 텍스트 필드 구조
                var isIdFocused by remember { mutableStateOf(false) }
                BasicTextField(
                    value = id,
                    onValueChange = { id = it },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isIdFocused) Color.Black else Color.LightGray
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart // 수직 정중앙 정렬로 글자 잘림 해결!
                        ) {
                            innerTextField()
                        }
                    }
                )
            }

            // 🎯 [비율 여백 3] 입력창 간격
            Spacer(modifier = Modifier.weight(1.8f))

            // Password 입력란 영역
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
                )

                // 🛠️ contentPadding 에러 원천 차단: 높이 고정에 최적화된 Custom 텍스트 필드 구조
                var isPwFocused by remember { mutableStateOf(false) }
                BasicTextField(
                    value = pw,
                    onValueChange = { pw = it },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isPwFocused) Color.Black else Color.LightGray
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart // 패스워드 마스킹 수직 정중앙 정렬
                        ) {
                            innerTextField()
                        }
                    }
                )
            }

            // 🎯 [비율 여백 4] 로그인 버튼 상단 여백
            Spacer(modifier = Modifier.weight(4.3f))

            // 로그인 버튼 (조건 미충족 시 클릭 방어, 색상은 초록색 고정유지)
            Button(
                onClick = {
                    if (id.isNotBlank() && pw.isNotBlank()) {
                        // 로그인 성공 로직 진입점
                    }
                },
                enabled = true, // 회색 변조 방지
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                shape = RoundedCornerShape(5.dp),
                elevation = null
            ) {
                Text(
                    text = "로그인",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 🎯 [비율 여백 5]
            Spacer(modifier = Modifier.weight(2.2f))

            // 하단 링크 영역
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

            // 🎯 최하단 바닥 밸런싱 여백
            Spacer(modifier = Modifier.weight(22.8f))

            // 키보드 대응 여백
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

    val cloverGreen = Color(0xFF99DE81)
    val disabledGray = Color(0xFFCCCCCC) // 버튼 비활성화용 배경색

    // 🎯 [실시간 회원가입 활성화 여부 검증 파이프라인]
    val isJoinEnabled = name.isNotBlank() &&
            id.isNotBlank() &&
            isIdChecked && isIdAvailable && // ID 중복확인 완료 필수
            pw.isNotBlank() &&
            pwConfirm.isNotBlank() &&
            (pw == pwConfirm) &&            // 비밀번호 재입력 일치 필수
            email.isNotBlank() &&
            !isEmailDuplicate &&            // 이메일 중복 없을 것 필수
            nickname.isNotBlank()

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎯 로고 위치 가이드 (Y:91)
            Spacer(modifier = Modifier.height(91.dp))
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
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

                Spacer(modifier = Modifier.height(15.dp))

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
                            .weight(1f)
                            .height(40.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    if (isIdChecked && isIdAvailable) {
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "사용 가능",
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Button(
                            onClick = {
                                isIdChecked = true
                                isIdAvailable = id.isNotBlank() && id != "test"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isIdChecked && !isIdAvailable) Color(0xFFFF6B6B) else cloverGreen
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

                // 🎯 비밀번호 실시간 검증 안내 문구 추가
                if (pw.isNotEmpty() && pwConfirm.isNotEmpty()) {
                    Text(
                        text = if (pw == pwConfirm) "비밀번호가 일치합니다." else "비밀번호가 일치하지 않습니다.",
                        color = if (pw == pwConfirm) Color(0xFF4CAF50) else Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, top = 4.dp)
                    )
                }

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

                // 이미 계정이 있으신가요?
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

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 모든 조건 만족 시에만 활성화되는 완료 버튼 적용
            Button(
                onClick = {
                    if (isJoinEnabled) {
                        navController.navigate("login")
                    }
                },
                enabled = isJoinEnabled, // 🌟 유효성 검사 결과에 따라 켜고 꺼짐
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cloverGreen,
                    disabledContainerColor = disabledGray // 꺼졌을 때 배경 회색 처리
                ),
                shape = RoundedCornerShape(5.dp),
                elevation = null
            ) {
                Text(
                    text = "완료",
                    color = if (isJoinEnabled) Color.Black else Color.White, // 꺼졌을 때 글씨 흰색 처리
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            }

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