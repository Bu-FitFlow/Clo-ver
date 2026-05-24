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

    // ⭐ [개선 포인트] 기존 복잡한 숫자를 버리고, 명확한 상태 2개로 완전히 분리
    var isIdChecked by remember { mutableStateOf(false) }     // 중복 확인을 한 번이라도 눌렀는가?
    var isIdAvailable by remember { mutableStateOf(false) }   // 그 아이디를 사용할 수 있는가?

    var isEmailDuplicate by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            // 하단에 고정된 완료 버튼
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 20.dp, vertical = 4.dp),
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
                .padding(paddingValues)
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

            // -----------------------------------------------------------------
            // ⭐ [ID 입력 및 중복 확인/체크마크 분리 영역]
            // -----------------------------------------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {
                CloverTextField(
                    value = id,
                    onValueChange = {
                        id = it
                        // 유저가 아이디를 타이핑하면 "중복 확인 안 한 상태"로 실시간 리셋!
                        isIdChecked = false
                        isIdAvailable = false
                    },
                    label = "ID",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))

                // 중복 확인 통과 여부에 따라 UI 컴포넌트 자체를 철저히 분리
                if (isIdChecked && isIdAvailable) {
                    // ① 중복 없음! 사용 가능할 땐 '체크 이미지'만 독립적으로 노출
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "사용 가능",
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    // ② 아직 누르지 않았거나 중복되어 통과를 못 했을 땐 '버튼'이 상시 대기
                    Button(
                        onClick = {
                            isIdChecked = true
                            // [임시 테스트용 규칙]: 아이디가 비어있지 않고 "test"가 아니면 통과!
                            // 나중에 조원들과 백엔드 합칠 때 이 자리에 서버 통신 로직을 넣으시면 됩니다.
                            isIdAvailable = id.isNotBlank() && id != "test"
                        },
                        colors = ButtonDefaults.buttonColors(
                            // 중복 실패 시 버튼을 경고 색상(레드)으로 피드백 변경
                            containerColor = if (isIdChecked && !isIdAvailable) Color(0xFFFF6B6B) else CloverGreen
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(40.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text(
                            text = if (isIdChecked && !isIdAvailable) "사용 불가 (재시도)" else "중복 확인",
                            color = if (isIdChecked && !isIdAvailable) Color.White else Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // [추가 디자인] 아이디가 중복되었을 때 텍스트 필드 밑에 노출되는 친절한 빨간색 경고 에러 가이드
            if (isIdChecked && !isIdAvailable) {
                Text(
                    text = "이미 사용 중이거나 유효하지 않은 아이디입니다.",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp)
                )
            }
            // -----------------------------------------------------------------

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

