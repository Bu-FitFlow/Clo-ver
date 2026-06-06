package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R
import com.fitflow.clover.mypage.MyPageUiState

@Composable
fun MyPageAccountProfileModify(
    navController: NavHostController,
    uiState: MyPageUiState = MyPageUiState(),
    onSaveNickname: (String) -> Unit = {}
) {
    var inputNickname by remember(uiState.nickname) {
        mutableStateOf(uiState.nickname)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(57.dp).background(Color.White))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterStart)
                        .clickable { navController.popBackStack() }
                )

                Text(
                    text = "계정 정보",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                Button(
                    onClick = {
                        onSaveNickname(inputNickname)
                        navController.popBackStack(
                            route = MyPageDestinations.ACCOUNT_PROFILE,
                            inclusive = false
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99DE81)),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = "완료",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AccountModifyInfoSection(label = "이름", value = uiState.name.ifBlank { "이름 정보 없음" })
                AccountModifyDivider()
                AccountModifyInfoSection(label = "이메일", value = uiState.email.ifBlank { "이메일 정보 없음" })
                AccountModifyDivider()

                Column {
                    Text(
                        text = "닉네임 변경",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputNickname,
                        onValueChange = { inputNickname = it },
                        placeholder = {
                            Text(
                                text = "새로운 닉네임을 입력하세요",
                                color = Color.LightGray,
                                fontSize = 20.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountModifyInfoSection(
    label: String,
    value: String
) {
    Column(modifier = Modifier.padding(start = 4.dp)) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
private fun AccountModifyDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(top = 8.dp)
            .height(1.dp)
            .background(Color.Black)
    )
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfileModifyPreview() {
    MyPageAccountProfileModify(navController = rememberNavController())
}
