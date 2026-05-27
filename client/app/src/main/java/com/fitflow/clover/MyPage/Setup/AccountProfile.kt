package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.R
import androidx.navigation.compose.rememberNavController


@Composable
fun MyPageAccountProfile(navController: NavController) {
    // 1. 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 2. 위에서 아래로 요소를 배치합니다. (Groovy의 LinearLayout vertical 느낌)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬
        ) {

            ProfileTopBar()

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
                    // 2. 이 아이콘은 Box의 자식이므로 CenterStart (왼쪽 중앙) 정렬이 가능합니다!
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
                        text = "계정 정보",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
                    )
                    // Spacer(modifier = Modifier.height(16.dp)) // 위아래 간격 띄우기
                }

                // 겹침 해결 핵심: 버튼들을 Column으로 한 번 더 묶고 spacedBy로 간격을 벌림
                Spacer(modifier = Modifier.height(20.dp)) // 위아래 간격 띄우기

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp) // 버튼 간의 간격 고정 (12dp)
                ) {
                    AccountProfileButton(label = "이름", value = "홍길동")
                    AccountProfileButton(label = "닉네임", value = "clover_user")
                    AccountProfileButton(label = "이메일", value = "clover@naver.com")
                }
            }
        }
    }
}

@Composable
fun AccountProfileButton(label : String, value : String) {
    // 💡 핵심: 버튼 내부에 가로를 꽉 채우는 투명한 Box를 만들고,
    // 그 안에서 내용물을 왼쪽 가운데(CenterStart)로 정렬합니다.
    Box(
        modifier = Modifier
            .size(width = 309.dp, height = 40.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF000000), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp), // 글자가 테두리에 너무 붙지 않게 안쪽 여백 살짝 주기
        contentAlignment = Alignment.CenterStart // 👈 왼쪽 정렬 시키는 마법의 키워드!
    ) {
        // 좌측에는 항목 이름, 우측에는 실제 값이 들어가도록 Row 배치
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // 양 끝으로 벌리기
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 항목 이름 (이름, 닉네임, 이메일)
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            // 실제 데이터 (DB나 State에서 받아올 값)
            Text(
                text = value,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



//상단바 상세 설정
@Composable
fun ProfileTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 요청하신 가로 사이즈
            .height(63.dp) // 요청하신 세로 사이즈
            .background(Color.White)
            .drawBehind {
                // 하단에 1dp 굵기의 회색 선을 그어 경계면을 확실히 함
                drawLine(
                    color = Color.White,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        contentAlignment = Alignment.Center // 내부 글자를 중앙 정렬
    ) {
    }
}

//미리보기 도화지 설정창
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfilePriview() {
    MyPageAccountProfile(navController = rememberNavController())
}