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
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
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
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R


@Composable
fun MyProfile(navController: NavController) {
    // 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단바
            MyProfileTopBar()

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
                            .align(Alignment.CenterStart)
                            .clickable{
                                navController.popBackStack()
                            }
                    )

                    Text(
                        text = "내 정보",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // 우측 [수정] 버튼 추가
                    Button(
                        onClick = {
                            // 🎯 클릭 시 프로필 수정 화면으로 이동하는 치트키 주입!
                            navController.navigate(MyPageDestinations.MYPROFILE_MODIFY)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF99DE81)
                        ),
                        shape = RoundedCornerShape(5.dp), // 살짝 각진 사각형 모양
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp), // 버튼 내 여백
                        modifier = Modifier
                            .align(Alignment.CenterEnd) // 오른쪽 정렬 🎯
                    ) {
                        Text(
                            text = "수정",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // 150 150 프로필 사진
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFFE0E0E0)) // 회색 배경
                        .border(1.dp, Color.Gray),     // 테두리
                    contentAlignment = Alignment.Center
                ){

                }

                // 프로필 사진 아래 ID 표기
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ID",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                // 키, 몸무게 가로배치
                // 그 아래 상하체 비만 여부, 얼굴형 아래로 배치
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // 요소 간격 고정
                ) {
                    // [키] / [몸무게] 가로 배치
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 키 입력 박스 (Row 안에서 반씩 나눠 갖도록 weight 1f 부여)
                        ProfileInfoBox(text = "키", modifier = Modifier.weight(1f))
                        // 몸무게 입력 박스
                        ProfileInfoBox(text = "몸무게", modifier = Modifier.weight(1f))
                    }

                    // [상하체 비만] 박스
                    ProfileInfoBox(text = "상하체 비만", modifier = Modifier.fillMaxWidth())

                    // [얼굴형] 박스
                    ProfileInfoBox(text = "얼굴형", modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun MyProfileTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(57.dp) // 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채움
    ) {
    }
}

@Composable
fun ProfileInfoBox(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ){
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

/*

@Composable
fun MyProfileTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(63.dp) // 세로 사이즈
            .background(Color.White)
            .drawBehind {
                // 하단에 1dp 굵기의 회색 선을 그어 경계면을 확실히 함
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        //contentAlignment = Alignment.Center // 내부 글자를 중앙 정렬
    ) {
    }
}
 */



//미리보기 도화지 설정창
//showBackground = true 미리보기 창 배경 흰색으로 설정
//device 미리보기 화면 크기 고정
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
//컴포즈 화면을 만드는 함수
@Composable
//미리보기 상자의 이름
fun MyProfilePreview() {
    MyProfile(navController = rememberNavController())
}
