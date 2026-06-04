package com.fitflow.clover.mypage.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.R

@Composable
fun NotificationScreen(
    onBackClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NotificationTopBar()
            // 1. 연두색 상단 타이틀 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .background(Color(0xFFEBF7E9)) // 시안의 연한 연두색 배경 반영
            ) {
                // 뒤로가기 화살표 버튼 (<)
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855), // 👈 에셋이 준비되면 ic_back 등으로 변경 가능
                    contentDescription = "뒤로가기 아이콘",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() },
                    tint = Color.Unspecified
                )

                // 중앙 '전체 알림' 타이틀
                Text(
                    text = "전체 알림",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 2. 단 한 건의 알림 아이템 영역 (시안 밀착 레이아웃)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 상품 이미지 (세로가 살짝 더 긴 직사각형 비율)
                    Image(
                        painter = painterResource(id = R.drawable.image_12), // 👈 여기에 알림 옷 이미지 리소스 ID 입력
                        contentDescription = "상품 이미지",
                        modifier = Modifier
                            .size(width = 75.dp, height = 85.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    // 우측 알림 텍스트
                    Text(
                        text = "고객님이 관심 있는 제품이 가격이 32000원에서\n30000원으로 가격이 내렸습니다!",
                        fontSize = 13.sp,
                        color = Color.Black,
                        lineHeight = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    )
                }

                // 알림 영역 바로 밑을 가로지르는 검은색 실선
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            // 3. 하단 나머지 공간은 아무것도 넣지 않아 시안처럼 깔끔한 흰색 빈 화면으로 둡니다.
        }
    }
}

@Composable
fun NotificationTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(57.dp) // 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채움
    ) {
    }
}

// 렌더링 확인용 미리보기 도화지
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        onBackClick = {}
    )
}