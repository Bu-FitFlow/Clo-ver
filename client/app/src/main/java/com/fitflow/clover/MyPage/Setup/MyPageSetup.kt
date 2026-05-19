package com.fitflow.clover.MyPage.Setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MyPageSetup() {
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
            SetupTopBar()

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
                    androidx.compose.material3.Icon(
                        painter = androidx.compose.ui.res.painterResource(com.fitflow.clover.R.drawable.kakaotalk_20260514_111630855),
                        contentDescription = "설정 아이콘",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart) // 🔥 Box 내부 정렬 규칙 적용
                    )

                    // 3. 이 텍스트도 Box의 자식이므로 Center (완전 중앙) 정렬이 가능합니다!
                    Text(
                        text = "설정",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
                    )
                    // Spacer(modifier = Modifier.height(16.dp)) // 위아래 간격 띄우기
                }

                // 3. 메뉴 버튼들 반복 배치
                SettingMenuButton(text = "내 계정")
                SettingTextItem(text = "내 정보")
                SettingTextItem(text = "계정 정보")
                SettingTextItem(text = "비밀번호 재설정")
                SettingMenuButton(text = "알림 설정")
                SettingTextItem(text = "푸쉬 알림 설정")
                SettingMenuButton(text = "개인 정보 처리 방침")
                SettingMenuButton(text = "탈퇴하기")
                SettingMenuButton(text = "로그아웃")
            }
        }
    }
}

//상단바 상세 설정
@Composable
fun SetupTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 요청하신 가로 사이즈
            .height(63.dp) // 요청하신 세로 사이즈
            .background(Color.Gray) // 배경을 흰색으로 채워 티가 나게 함
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

// 테두리가 있는 버튼 모양을 만드는 커스텀 컴포넌트
@Composable
fun SettingMenuButton(text: String) {
    Button(
        onClick = { /* 클릭 이벤트 */ },
        modifier = Modifier
            .fillMaxWidth() // 가로 꽉 채우기
            .padding(vertical = 8.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)), // 검은색 테두리
        colors = ButtonDefaults.buttonColors(containerColor = Color.White), // 배경은 흰색
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text, color = Color.Black)
    }
}

// 테두리가 없는 일반 텍스트 메뉴
@Composable
fun SettingTextItem(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(vertical = 12.dp)
    )
}

//미리보기 도화지 설정창
//showBackground = true 미리보기 창 배경 흰색으로 설정
//device 미리보기 화면 크기 고정
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
//컴포즈 화면을 만드는 함수
@Composable
//미리보기 상자의 이름
fun MyPageSetupPreview() {
    //Column 위에서 아래로 요소를 쌓는 상자
   Column() {
       // 가로 134.5, 세로 13dp 짜리 작고 납작한 회색 사각형을 화면 맨 위에 그림
       // 상단바 영역을 채워둔 것
       Box(
           modifier = Modifier
               .width(134.5.dp)
               .height(13.dp)
               .background(Color.White) // 영역 확인을 위해 배경색 추가
       )
   }
    // 진짜 화면 불러오기
    MyPageSetup()
}

