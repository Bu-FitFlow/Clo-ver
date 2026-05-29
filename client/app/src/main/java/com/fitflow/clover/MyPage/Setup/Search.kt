package com.fitflow.clover.domain

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.fitflow.clover.R

@Composable
fun Search() {
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
            //SearchTopBar()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)               // 세로 높이 57dp 적용
                    .background(Color(0x3399DE81)) // 99DE81 색상으로 배경 채우기
            )

            {
                // 2. 이 아이콘은 Box의 자식이므로 CenterStart (왼쪽 중앙) 정렬이 가능합니다!
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                    contentDescription = "뒤로가기 아이콘",
                    modifier = Modifier
                        .padding(start=16.dp)
                        .size(28.dp)
                        .align(Alignment.CenterStart) // 🔥 Box 내부 정렬 규칙 적용

                )

                Icon(
                    painter = painterResource(R.drawable.clover),
                    contentDescription = "클로버 아이콘",
                    modifier = Modifier
                        .width(94.dp)  // 가로 크기
                        .height(62.dp) // 세로 크기
                        .align(Alignment.Center), // 🔥 Box 내부 정렬 규칙 적용
                    tint = Color.Unspecified // 원래 이미지 색상 그대로 나오게 함

                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp) //양옆에 31dp씩 여백
                    .padding(top=16.dp)
                    .height(44.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 검색창 힌트 텍스트 (원하는 문구로 바꾸거나 TextField를 넣으시면 됩니다)
                    androidx.compose.material3.Text(
                        text = "검색어를 입력하세요",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Icon(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = "검색 아이콘",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified // 원래 이미지 색상 그대로 나오게 함

                    )
                }
            } //검색 바 Box

            // 최근 검색 / 검색 기록 삭제 글씨 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp) // 검색바와 똑같이 양옆에 31dp 여백을 주어 라인을 맞춥니다.
                    .padding(top = 24.dp),       // 검색바와의 위쪽 간격
                horizontalArrangement = Arrangement.SpaceBetween, // 좌우 양끝 정렬
                verticalAlignment = Alignment.Bottom              // 글씨 아랫날짜 기준 정렬
            ) {
                Text(
                    text = "최근 검색",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "검색 기록 삭제",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable {
                        // 클릭 시 전체 삭제 기능
                    }
                )
            }

            // 길이 331짜리 선
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                    .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                    .height(1.dp)                 // 두께 (1dp)
                    .background(Color.Black)      // 색상
            )
        }
    }
}
/*

@Composable
fun SearchTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(63.dp) // 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채움
    ) {
    }
}

 */
//미리보기 도화지 설정창
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun SearchPreview() {

    Search()
}