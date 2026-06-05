package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageAccountProfileModify(
    navController: NavHostController,
    viewModel: AccountProfileModifyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 빈 바
            Box(modifier = Modifier.fillMaxWidth().height(57.dp).background(Color.White))

            // 타이틀 영역
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

                // 우측 [완료] 버튼
                Button(
                    onClick = {
                        // 뷰모델에 입력한 닉네임 최종 확정 및 저장!
                        viewModel.saveNickname()
                        // 🎯 클릭 시 이전에 만든 프로필 수정 화면(ACCOUNT_PROFILE_MODIFY)으로 다이렉트 이동
                        navController.navigate(MyPageDestinations.SETUP)
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

            // 입력 및 정보 표시 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp) // 비밀번호 창과 동일한 간격
            ) {
                // 1. 이름 (수정 불가 - 라벨 텍스트 스타일)
                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(text = "이름",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = uiState.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                        .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                        .height(1.dp)                 // 두께 (1dp)
                        .background(Color.Black)      // 색상
                )

                // 2. 이메일 (수정 불가 - 라벨 텍스트 스타일)
                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(text = "이메일",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = uiState.email,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                        .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                        .height(1.dp)                 // 두께 (1dp)
                        .background(Color.Black)      // 색상
                )


                // 3. 닉네임 변경 입력창 (🎯 비밀번호 입력창과 완전히 동일한 스타일의 테두리 박스!)
                Column {
                    Text(
                        text = "닉네임 변경",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.inputNickname, // 뷰모델의 임시 입력 상태와 직결
                        onValueChange = { viewModel.onNicknameChanged(it) }, // 타이핑할 때마다 데이터 업데이트
                        placeholder = {
                            Text(
                                text = "새로운 닉네임을 입력하세요",
                                color = Color.LightGray,
                                fontSize = 20.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp), // 살짝 둥근 테두리
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,      // 클릭(포커스) 시 테두리 검은색
                            unfocusedBorderColor = Color.Black,    // 평상시 테두리 검은색
                            focusedContainerColor = Color.White,   // 내부 배경 흰색
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color.Black              // 깜빡이는 커서 검은색
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfileModifyPreview() {
    MyPageAccountProfileModify(navController = rememberNavController())
}