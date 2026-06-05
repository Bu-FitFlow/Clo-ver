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
fun MyPageAccountProfile(
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
                        // 🎯 클릭 시 이전에 만든 프로필 수정 화면(ACCOUNT_PROFILE_MODIFY)으로 다이렉트 이동
                        navController.navigate(MyPageDestinations.ACCOUNT_PROFILE_MODIFY)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99DE81)),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = "수정",
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
                    Text(
                        text = "이름",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
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
                    Text(
                        text = "이메일",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.email,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                        .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                        .height(1.dp)                 // 두께 (1dp)
                        .background(Color.Black)      // 색상
                )


                Column(modifier = Modifier.padding(start = 4.dp)) {
                    Text(
                        text = "닉네임",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uiState.nickname,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                        .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                        .height(1.dp)                 // 두께 (1dp)
                        .background(Color.Black)      // 색상
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfilePreview() {
    MyPageAccountProfile(navController = rememberNavController())
}

/*
package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

@Composable
fun MyPageAccountProfile(
    navController: NavHostController,
    // 💡 뷰모델 객체를 주입받습니다.
    viewModel: AccountProfileModifyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // 💡 뷰모델의 uiState 실시간 관찰 조각
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyPageAccountProfileTopBar()

            // 1. 상단 타이틀 및 수정 버튼 영역
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
                        .align(Alignment.CenterStart)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "계정 정보",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                // 우측 [수정] 버튼
                Button(
                    onClick = {
                        // 🎯 클릭 시 이전에 만든 프로필 수정 화면(ACCOUNT_PROFILE_MODIFY)으로 다이렉트 이동
                        navController.navigate(MyPageDestinations.ACCOUNT_PROFILE_MODIFY)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99DE81)),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = "수정",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. 계정 정보 리스트 배치 영역 (뷰모델의 uiState 값을 바인딩)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AccountInfoBlock(label = "이름", value = uiState.name.ifEmpty { "-" },)
                AccountInfoBlock(label = "이메일", value = uiState.email.ifEmpty { "-" },)
                AccountInfoBlock(label = "닉네임", value = uiState.nickname.ifEmpty { "-" },)
            }
        }
    }
}

@Composable
fun AccountInfoBlock(label: String, value: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // 요청하신 버튼 높이 사양 반영
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF000000), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MyPageAccountProfileTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)
            .height(57.dp)
            .background(Color.White)
    ) {

    }
}


// 🔍 미리보기용 프리뷰 도화지
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfilePreview() {
    MyPageAccountProfile(navController = rememberNavController())
}
 */