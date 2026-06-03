package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

// 💡 회원가입 시 입력했던 정보를 담아두는 데이터 바구니 (Data Class)
data class UserData(
    val name: String,
    val nickname: String,
    val email: String
)

@Composable
fun MyPageAccountProfile(
    navController: NavHostController,
    userData: UserData? = null // 💡 회원가입 시 입력된 데이터를 외부(서버/뷰모델)에서 받아옵니다.
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. 상단 타이틀 영역 (기존 화면과 디자인 통일)
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
            }

            // 2. 계정 정보 리스트 배치 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp) // 각 항목 간의 간격
            ) {
                InfoRowItem(label = "이름", value = userData?.name ?: "-")
                InfoRowItem(label = "닉네임", value = userData?.nickname ?: "-")
                InfoRowItem(label = "이메일", value = userData?.email ?: "-")
            }
        }
    }
}

// 📌 이름, 닉네임, 이메일을 각각 한 줄씩 이쁘게 그려줄 재사용 컴포넌트
@Composable
fun InfoRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White, // 살짝 연한 회색 배경으로 고급스러운 느낌 추가
                shape = RoundedCornerShape(8.dp) // 테두리 라운딩 통일
            )
            .border(
                width = 1.dp,        // 테두리 두께 1dp
                color = Color.Black, // 테두리 색상 검은색 (비밀번호 창과 동일)
                shape = RoundedCornerShape(8.dp) // 테두리도 똑같이 둥글게 처리
            )
            .padding(horizontal = 16.dp, vertical = 18.dp), // 내부 여백
        horizontalArrangement = Arrangement.SpaceBetween, // 라벨은 왼쪽, 데이터는 오른쪽에 배치
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 항목 이름 (이름, 닉네임, 이메일 타이틀)
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        // 실제 유저 데이터 값
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

// 🔍 미리보기용 데이터 세팅
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfilePreview() {

    MyPageAccountProfile(
        navController = rememberNavController(),
        userData = null
    )
}
/*
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

            //ProfileTopBar()

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
            .size(width = 309.dp, height = 50.dp)
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


/*

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

 */

//미리보기 도화지 설정창
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountProfilePriview() {
    MyPageAccountProfile(navController = rememberNavController())
}
 */