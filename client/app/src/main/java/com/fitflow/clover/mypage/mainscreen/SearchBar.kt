package com.fitflow.clover.mypage.mainscreen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.R

@Composable
fun Search(onBackClick: () -> Unit = {},
           onSearchExecute: (String) -> Unit = { query -> println("기본 검색 실행 로그: $query")})
{
    var searchQuery by remember { mutableStateOf("") }
    val recentSearches = remember { mutableStateListOf<String>() }

    // 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.Companion.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.Companion.fillMaxSize(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            // 상단바
            SearchTopBar()

            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(57.dp)               // 세로 높이 57dp 적용
                    .background(Color(0x3399DE81)) // 99DE81 색상으로 배경 채우기
            )

            {
                // 2. 이 아이콘은 Box의 자식이므로 CenterStart (왼쪽 중앙) 정렬이 가능합니다!
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                    contentDescription = "뒤로가기 아이콘",
                    modifier = Modifier.Companion
                        .padding(start = 16.dp)
                        .size(28.dp)
                        .align(Alignment.Companion.CenterStart)
                        .clickable { onBackClick() }// 🔥 Box 내부 정렬 규칙 적용

                )

                Icon(
                    painter = painterResource(R.drawable.clover),
                    contentDescription = "클로버 아이콘",
                    modifier = Modifier.Companion
                        .width(94.dp)  // 가로 크기
                        .height(62.dp) // 세로 크기
                        .align(Alignment.Companion.Center), // 🔥 Box 내부 정렬 규칙 적용
                    tint = Color.Companion.Unspecified // 원래 이미지 색상 그대로 나오게 함

                )
            } // 상단 BOX 끝

            // 🔍 [수정됨] 실제 입력이 가능한 검색 바 영역
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
                    .padding(top = 16.dp)
                    .height(44.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Companion.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Companion.CenterStart
            ) {
                // 🔥 BasicTextField를 사용해 커스텀 디자인 내부에서 입력이 가능하도록 구현
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }, // 글자가 입력될 때마다 상태 업데이트
                    modifier = Modifier.Companion.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Companion.Black,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(Color.Companion.Black), // 커서 색상 설정
                    singleLine = true, // 한 줄만 입력 가능하도록 제한
                    decorationBox = { innerTextField ->
                        // 텍스트 필드 내부의 레이아웃을 Row로 배치
                        Row(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            verticalAlignment = Alignment.Companion.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(modifier = Modifier.Companion.weight(1f)) {
                                // 힌트 텍스트: 입력된 글자가 없을 때만 보여줌
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "검색어를 입력하세요",
                                        color = Color.Companion.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField() // 실제 글자가 입력되는 눈에 안 보이는 영역
                            }

                            // 검색 아이콘
                            Icon(
                                painter = painterResource(R.drawable.search_icon),
                                contentDescription = "검색 아이콘",
                                modifier = Modifier.Companion
                                    .size(24.dp)
                                    .clickable {
                                        // 돋보기 아이콘 눌렀을 때 검색 실행할 로직
                                        if (searchQuery.isNotBlank()) {
                                            val trimmedQuery = searchQuery.trim()

                                            // 🔥 [기능 추가] 최근 검색어 리스트 맨 앞에 추가 (중첩)
                                            // 중복된 검색어가 들어오면 기존 것을 지우고 맨 앞으로 보내는 센스!
                                            if (recentSearches.contains(trimmedQuery)) {
                                                recentSearches.remove(trimmedQuery)
                                            }
                                            recentSearches.add(0, trimmedQuery)

                                            // 기존 화면 이동/로그 로직 실행
                                            onSearchExecute(trimmedQuery)

                                            // 검색 후 입력창 비워주기 (선택 사항, 원치 않으면 주석 처리하세요)
                                            searchQuery = ""
                                        }
                                    },
                                tint = Color.Companion.Unspecified
                            )
                        }
                    }
                )
            } // 검색 바 Box 끝


            // 최근 검색 / 검색 기록 삭제 글씨 영역
            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp) // 검색바와 똑같이 양옆에 31dp 여백을 주어 라인을 맞춥니다.
                    .padding(top = 24.dp),       // 검색바와의 위쪽 간격
                horizontalArrangement = Arrangement.SpaceBetween, // 좌우 양끝 정렬
                verticalAlignment = Alignment.Companion.Bottom              // 글씨 아랫날짜 기준 정렬
            ) {
                Text(
                    text = "최근 검색",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color.Companion.Black
                )
                Text(
                    text = "검색 기록 삭제",
                    fontSize = 12.sp,
                    color = Color.Companion.Gray,
                    modifier = Modifier.Companion.clickable {
                        // 클릭 시 전체 삭제 기능
                        recentSearches.clear()
                    }
                )
            }

            // 길이 331짜리 선
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp) // 양옆 31dp 여백을 주면 화면 크기(393) 기준 자동으로 가로 331dp가 됩니다!
                    .padding(top = 8.dp)          // 글씨 영역과의 위쪽 간격
                    .height(1.dp)                 // 두께 (1dp)
                    .background(Color.Companion.Black)      // 색상
            )

            // --- 4. 🔥 [새로 추가됨] 최근 검색어 목록 표시 영역 ---
            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp) // 검색어 간의 간격
            ) {
                // 저장된 최근 검색어 리스트를 돌면서 하나씩 화면에 그려줌
                recentSearches.forEach { keyword ->
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        // 검색어 텍스트 (누르면 해당 단어로 재검색도 가능하도록 설정 가능)
                        Text(
                            text = keyword,
                            fontSize = 14.sp,
                            color = Color.Companion.DarkGray,
                            modifier = Modifier.Companion.clickable {
                                searchQuery = keyword // 최근 검색어를 누르면 입력창에 입력됨
                            }
                        )

                        // 개별 삭제 버튼 (X 표시)
                        Text(
                            text = "✕",
                            fontSize = 12.sp,
                            color = Color.Companion.LightGray,
                            modifier = Modifier.Companion
                                .padding(4.dp)
                                .clickable {
                                    recentSearches.remove(keyword) // 리스트에서 해당 단어만 삭제
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTopBar() {
    Box(
        modifier = Modifier.Companion
            .width(393.dp)  // 가로 사이즈
            .height(57.dp) // 세로 사이즈
            .background(Color.Companion.White) // 배경을 흰색으로 채움
    ) {
    }
}

//미리보기 도화지 설정창
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun SearchPreview() {

    Search()
}
