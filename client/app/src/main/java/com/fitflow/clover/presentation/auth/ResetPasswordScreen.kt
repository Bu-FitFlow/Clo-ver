package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.core.component.CustomOutlinedInput
import com.fitflow.clover.R
import androidx.compose.ui.tooling.preview.Preview

// 4. 새 비밀번호 입력 화면 (수정 완료)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var newPw by remember { mutableStateOf("") }
    var confirmPw by remember { mutableStateOf("") }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text("새 비밀번호 입력", fontSize = 28.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(60.dp))
            CustomOutlinedInput(value = newPw, onValueChange = { newPw = it }, placeholder = "새 password")
            Spacer(modifier = Modifier.height(15.dp))
            CustomOutlinedInput(value = confirmPw, onValueChange = { confirmPw = it }, placeholder = "새 password 재입력")

            Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), contentAlignment = Alignment.Center) {
                Text("아이디를 잊으셨나요?", color = Color.Red, fontSize = 12.sp, modifier = Modifier.clickable {
                    navController.navigate("find_id_pw")
                }, textAlign = TextAlign.Center, textDecoration = TextDecoration.Underline)
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) { Text("로그인으로", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        }
    }
}