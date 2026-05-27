package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R

@Composable
fun MyPageAccountPassword(navController: NavHostController) {

    // 💡 사용자가 입력한 값을 기억하는 변수 3개 (프론트엔드 자체 UI 상태)
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 1. 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 2. 위에서 아래로 요소를 배치합니다. (Groovy의 LinearLayout vertical 느낌)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            //.padding(16.dp), // iPhone 16 피그마 수치대로 여백 주기
            horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬

        ) {
            PasswordTopBar()

            // 위에서 아래로 요소를 배치 (Groovy의 LinearLayout vertical 느낌)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {

                    Icon(
                        painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                        contentDescription = "뒤로가기 아이콘",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart) // 🔥 Box 내부 정렬 규칙 적용
                            .clickable{
                                navController.popBackStack()
                            }
                    )

                    // 3. 이 텍스트도 Box의 자식이므로 Center (완전 중앙) 정렬이 가능합니다!
                    Text(
                        text = "비밀번호 재설정",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
                    )

                }

                // 겹침 해결 핵심: 버튼들을 Column으로 한 번 더 묶고 spacedBy로 간격을 벌림
                Spacer(modifier = Modifier.height(20.dp)) // 위아래 간격 띄우기

                // 💡 입력창들을 수직으로 배치
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    PasswordInputField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        hintText = "현재 비밀번호 입력"
                    )
                    PasswordInputField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        hintText = "새 비밀번호 입력"
                    )
                    PasswordInputField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        hintText = "새 비밀번호 재입력"
                    )
                }
            }
        }
    }
}

//상단바 상세 설정
@Composable
fun PasswordTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 요청하신 가로 사이즈
            .height(63.dp) // 요청하신 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채워 티가 나게 함
            .drawBehind {
                // 하단에 1dp 굵기의 회색 선을 그어 경계면을 확실히 함
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        contentAlignment = Alignment.Center // 내부 글자를 중앙 정렬
    ) {
    }
}

// 💡 버튼 대신 실제 '입력'을 받을 수 있도록 만든 커스텀 패스워드 입력창 컴포넌트
@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.size(width = 309.dp, height = 50.dp), // 기존 디자인 비율 유지 (높이만 입력창에 맞게 50 수정)
        placeholder = {
            Text(text = hintText, color = Color.Gray, fontSize = 14.sp)
        },
        singleLine = true,
        // 🔒 비밀번호를 ●●● 로 가려주는 마법의 코드
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,       // 포커싱 되었을 때 테두리 색
            unfocusedBorderColor = Color.Black,     // 포커싱 안 되었을 때 테두리 색
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}


/*

// 2. ⭐️ 버튼 안의 글자를 왼쪽으로 밀어주는 커스텀 버튼 코드
@Composable
fun PasswordMenuButton(text: String) {
    Button(
        onClick = { /* 클릭 이벤트 */ },
        modifier = Modifier
            .size(width = 309.dp, height = 40.dp) // 가로 309 세로 40
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp) // 🎯 세로 높이가 낮을 때 글자 잘림 방지
    ) {
        // 💡 핵심: 버튼 내부에 가로를 꽉 채우는 투명한 Box를 만들고,
        // 그 안에서 내용물을 왼쪽 가운데(CenterStart)로 정렬합니다.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // 글자가 테두리에 너무 붙지 않게 안쪽 여백 살짝 주기
            contentAlignment = Alignment.CenterStart // 👈 왼쪽 정렬 시키는 마법의 키워드!
        ) {
            Text(text = text, color = Color.Black)
        }
    }
}

 */

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountPasswordPreview() {
    MyPageAccountPassword(navController = rememberNavController())
}