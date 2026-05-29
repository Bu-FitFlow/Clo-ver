package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R

@Composable
fun PersonalInformation(navController: NavController) {
    // 1. 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 2. 위에서 아래로 요소를 배치합니다. (Groovy의 LinearLayout vertical 느낌)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            //.padding(16.dp), // iPhone 16 피그마 수치대로 여백 주기
            horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬

        ) {
            //PersonalInformationTopBar()

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
                            // 백스택의 가장 위 화면을 날려서 이전 화면(MyPageSetup)으로 이동시킵니다.
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "개인정보 처리방침",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
                )
            }

            // 🎯 Column 대신 LazyColumn 사용 (자동으로 스크롤이 지원됨)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp)
                /*
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬
                 */
            ) {
                // 문단별로 item을 나누어 배치하면 관리가 편해집니다.
                item {
                    Text(
                        text = "제 1 조 (개인정보 수집 및 이용 목적)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = """
                            1. 회원 가입 및 관리: 본인 확인, 부정 이용 방지
                            2. 서비스 제공: 게시글 등록, 채팅 서비스, 위치 기반 근거리 매물 추천
                            3. 결제 및 배송: 택배 거래 시 주소 및 연락처 확인 (결제 수단 정보)
                            
                        """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "제 2 조 (수집하는 개인정보 항목)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = """
                            1. 필수 : 이메일(ID), 비밀번호, 닉네임, 연락처
                            2. 선택 : 프로필 사진
                            3. 자동 수집 : 쿠키, 서비스 이용 기록, 기기 정보
                            
                        """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "제 3 조 (개인정보의 보유 및 이용 기간)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = """
                            1. 원칙 : 회원 탈퇴 시 즉시 파기
                            2. 예외 : 부정 거래 방지를 위해 탈퇴 후 6개월간 보관하거나, 전자상거래법 등 법령에 따라 5년간 보관할 수 있음
                            
                        """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "제 4 조 (제3자 제공 및 위탁)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = """
                            1. 제3자 제공 : 구매자와 판매자가 택배 거래를 할 때 서로의 주소와 연락처를 공유하는 행위
                            2. 위탁 : 알림톡 발송(카카오), 결제 대행(Toss/NHN KCP), 위치 정보 서비스(Google Maps API 등)
                        """.trimIndent(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                // 하단 여백용 아이템
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            } // LazyColumn () {}
        }
    }
}


/*
//상단바 상세 설정
@Composable
fun PersonalInformationTopBar() {
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
        contentAlignment = Alignment.Center // 내부 글자를 중앙 정렬
    ) {
    }
}

 */

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun PersonalInformationPreview() {
    // 진짜 화면 불러오기
    MyPageAccountProfile(navController = rememberNavController())
}