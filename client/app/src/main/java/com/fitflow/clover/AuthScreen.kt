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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

@Composable
fun JoinDetail(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var pwConfirm by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    // 중복 확인 상태: 0(기본), 1(성공/체크), 2(실패/빨간버튼)
    var idCheckStatus by remember { mutableIntStateOf(0) }
    // 이메일 중복 경고 표시 여부
    var isEmailDuplicate by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .padding(horizontal = 35.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            // 로고 이미지
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))

            // 이름 입력
            CloverTextField(value = name, onValueChange = { name = it }, label = "이름")
            Spacer(modifier = Modifier.height(10.dp))

            // ID 입력 및 중복 확인 버튼 섹션
            Row(verticalAlignment = Alignment.CenterVertically) {
                CloverTextField(
                    value = id,
                    onValueChange = { id = it; idCheckStatus = 0 },
                    label = "ID",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))

                // 상태에 따른 버튼/아이콘 변화 로직
                when (idCheckStatus) {
                    1 -> { // 성공: 초록색 체크 표시
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    2 -> { // 실패: 빨간색 중복 버튼
                        Button(
                            onClick = { idCheckStatus = 1 }, // 테스트용: 클릭 시 성공으로 변경
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(40.dp)
                        ) { Text("중복", color = Color.White, fontSize = 12.sp) }
                    }
                    else -> { // 기본: 중복 확인 버튼
                        Button(
                            onClick = { idCheckStatus = 2 }, // 테스트용: 클릭 시 실패로 변경
                            colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(40.dp)
                        ) { Text("중복 확인", color = Color.Black, fontSize = 11.sp) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            CloverTextField(value = pw, onValueChange = { pw = it }, label = "password")
            Spacer(modifier = Modifier.height(10.dp))
            CloverTextField(value = pwConfirm, onValueChange = { pwConfirm = it }, label = "password 재입력")
            Spacer(modifier = Modifier.height(10.dp))
            // 이메일 입력 (테스트를 위해 "test" 입력 시 중복 메시지 출력)
            CloverTextField(
                value = email,
                onValueChange = { email = it; isEmailDuplicate = (it == "test") },
                label = "이메일"
            )
            Spacer(modifier = Modifier.height(10.dp))
            CloverTextField(value = nickname, onValueChange = { nickname = it }, label = "닉네임")

            Spacer(modifier = Modifier.height(60.dp))

            Spacer(modifier = Modifier.height(50.dp))

// "이미 계정이 있으신가요?" 텍스트 버튼으로 변경
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "이미 계정이 있으신가요?",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "로그인",
                    fontSize = 14.sp,
                    color = Color.Blue, // 강조를 위해 파란색 적용
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            // 로그인 화면으로 이동하며 백스택 정리
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                )
            }

            // 이메일 중복 시 나타나는 메시지
            if (isEmailDuplicate) {
                Text(
                    text = "중복 된 이메일 입니다.",
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            // 완료 버튼
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("완료", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
