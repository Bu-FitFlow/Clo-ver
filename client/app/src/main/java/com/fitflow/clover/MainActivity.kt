package com.fitflow.clover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
<<<<<<< HEAD
            // MaterialTheme로 감싸줘야 기본 디자인이 먹힘
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignupScreen()
=======
            CloverTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "이렇게 하면 업데이트가 되는 건가?",
                        modifier = Modifier.padding(innerPadding)
                    )
>>>>>>> ede9787de6f7b3ab630986a87c9cc98c7b42e6e8
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    // 입력창 상태 저장하는 변수들
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // 양옆 여백 좀 넉넉하게 줌
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: 여기에 그 2D 벡터 클로버 로고 이미지 넣으면 됨
        Text(
            text = "CLO-VER",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // 이메일 입력창
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호 입력창
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
            // TODO: 나중에 비번 안 보이게 마스킹 처리(VisualTransformation) 추가해야 됨
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 로그인 버튼
        Button(
            onClick = { /* TODO: Spring Boot 서버로 로그인 쏘는 로직 */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("로그인", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회원가입 & 비번찾기
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { /* TODO: 회원가입 화면 이동 */ }) {
                Text("회원가입")
            }
            TextButton(onClick = { /* TODO: 비밀번호 찾기 화면 이동 */ }) {
                Text("비밀번호 찾기")
            }
        }
    }
}