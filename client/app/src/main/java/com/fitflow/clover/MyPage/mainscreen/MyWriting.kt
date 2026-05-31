package com.fitflow.clover.mypage.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.fitflow.clover.mypage.setup.MyProfileModify


@Composable
fun MyWriting(navController: NavHostController) {

    // 현재 어떤 탭이 선택되어 있는지 저장하는 상태 변수 (기본값: "전체")
    var selectedCategory by remember { mutableStateOf("전체") }
    val categories = listOf("전체", "자유", "리뷰", "코디")

// 1. 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                        .align(Alignment.CenterStart) // 🔥 Box 내부 정렬 규칙 적용
                        .clickable {
                            navController.popBackStack()
                        }
                )

                // 3. 이 텍스트도 Box의 자식이므로 Center (완전 중앙) 정렬이 가능합니다!
                Text(
                    text = "내가 쓴 글",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
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

            Spacer(modifier = Modifier.height(12.dp))

            // [2] 카테고리 탭 영역 (가로 스크롤 가능하도록 세팅)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp) // 버튼 간의 간격
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            // 선택되었을 때는 연초록색(99DE81), 아닐 때는 흰색 배경
                            .background(
                                color = if (isSelected) Color(0xFF99DE81) else Color.White,
                                shape = RoundedCornerShape(17.dp)
                            )
                            .border(1.dp, Color.Black, RoundedCornerShape(17.dp))
                            .clickable { selectedCategory = category },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 길이 331짜리 선
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                    .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                    .height(1.dp)                 // 두께 (1dp)
                    .background(Color.Black)      // 색상
            )

            Spacer(modifier = Modifier.height(16.dp))

            // [3] 하단 작성 글 리스트 영역 (테두리가 있는 큰 박스 구조)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)) // 바깥 둥근 사각형 테두리
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                // 시안처럼 6개의 행을 생성합니다.
                repeat(6) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp) // 각 행의 세로 높이
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End, // 점 3개를 오른쪽 끝으로 밀기
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 💡 추후 여기에 글 제목 등의 Text 컴포넌트를 배치하시면 됩니다.
                        // Text(text = "작성한 글 제목이 들어갑니다.", modifier = Modifier.weight(1f), fontSize = 14.sp)

                        // 우측 끝의 점 3개 아이콘 (...)
                        Text(
                            text = "···",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                /* TODO: 글 수정/삭제 등의 바텀시트나 메뉴 팝업 트리거 */
                            }
                        )
                    }

                    // 마지막 아이템 아래에는 내부 선을 그리지 않음
                    if (index < 5) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Gray) // 내부 칸막이 선
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
//컴포즈 화면을 만드는 함수
@Composable
//미리보기 상자의 이름
fun MyWritingPreview() {
    // 진짜 화면 불러오기
    MyWriting(navController = rememberNavController())
}
