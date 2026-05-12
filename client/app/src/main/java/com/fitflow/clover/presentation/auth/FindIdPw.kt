package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.fitflow.clover.CloverGreen
import com.fitflow.clover.core.component.CloverTextField
import com.fitflow.clover.R
import com.fitflow.clover.core.component.CloverButton


// 1. 아이디/비밀번호 찾기 (수정 완료)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindIdPw(navController: NavController) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var idInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var authCodeInput by remember { mutableStateOf("") }

    // 로직 상태 변수
    var isAuthSent by remember { mutableStateOf(false) } // 인증 버튼 클릭 여부
    var showIdResult by remember { mutableStateOf(false) } // 아이디 결과창 표시 여부

    Scaffold(
        containerColor = Color.White, // 배경색 흰색 고정
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text(if (tabIndex == 0) "아이디 찾기" else "비밀번호 찾기", fontSize = 28.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {

            if (!showIdResult) {
                // 탭 버튼
                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 60.dp)) {
                    CloverButton("아이디 찾기", tabIndex == 0, Modifier.weight(1f)) {
                        tabIndex = 0; isAuthSent = false
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    CloverButton("비밀번호 찾기", tabIndex == 1, Modifier.weight(1f)) {
                        tabIndex = 1; isAuthSent = false
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                if (tabIndex == 1) {
                    CloverTextField(value = idInput, onValueChange = { idInput = it }, label = "ID")
                    Spacer(modifier = Modifier.height(12.dp))
                }
                CloverTextField(value = nameInput, onValueChange = { nameInput = it }, label = "이름 입력")
                Spacer(modifier = Modifier.height(12.dp))
                CloverTextField(value = emailInput, onValueChange = { emailInput = it }, label = "이메일 입력")
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    CloverTextField(value = authCodeInput, onValueChange = { authCodeInput = it }, label = "인증번호 6자리", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { isAuthSent = true },
                        modifier = Modifier.height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text(if (isAuthSent) "재인증" else "인증받기", color = Color.Black, fontSize = 12.sp)
                    }
                }

                // 인증 버튼을 누른 후에만 메시지 표시
                if (isAuthSent) {
                    Text("인증번호가 오지 않았습니까?", color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (tabIndex == 0) showIdResult = true
                        else navController.navigate("reset_password")
                    },
                    modifier = Modifier.fillMaxWidth().height(62.dp).padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) { Text("확인", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.height(200.dp))

            } else {
                // 아이디 알려주는 창 (결과 화면)
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("ID", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(50.dp).border(1.dp, Color.Gray).padding(vertical = 13.dp), contentAlignment = Alignment.Center) {
                        Text("user_id_example", fontSize = 16.sp) // 실제 아이디 데이터 바인딩 지점
                    }
                    Text("비밀번호를 잊으셨나요?", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp).clickable {
                        showIdResult = false; tabIndex = 1
                    })
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.fillMaxWidth().height(62.dp).padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("로그인으로", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
