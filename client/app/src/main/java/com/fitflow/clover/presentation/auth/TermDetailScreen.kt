package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermDetailScreen(navController: NavController, title: String) {

    // 🎯 [요구사항] 타이틀에 따라 각각 다른 상세 내용이 나오도록 분기 처리
    val termContent = when (title) {
        "이용약관 동의(필수)" -> {
            "제 1 조 (목적)\n본 약관은 Clover 플랫폼이 제공하는 중고 의류 거래 서비스의 이용 조건 및 절차를 규정함을 목적으로 합니다.\n\n제 2 조 (회원의 의무)\n회원은 회사가 정한 약관 및 관계 법령을 준수해야 하며, 타인의 정보를 도용해서는 안 됩니다."
        }
        "개인정보 수집 및 이용동의(필수)" -> {
            "1. 수집하는 개인정보 항목: 이름, 아이디, 비밀번호, 이메일, 닉네임\n2. 수집 및 이용 목적: 회원 가입 및 식별, Clover 서비스 내 중고 의류 거래 프로세스 진행\n3. 보유 및 이용 기간: 회원 탈퇴 시까지 혹은 관계 법령에 따른 보존 기간까지"
        }
        else -> "상세 약관 정보가 존재하지 않습니다."
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // 상단 커스텀 헤더 영역 (Y:63 눈높이 매핑 유지)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 15.dp) // Y:63 선상 배치를 위한 상단 패딩
                    .height(48.dp)
            ) {
                // 뒤로가기 아이콘 (크기 40*40, 위치 X:27)
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = 27.dp)
                        .size(40.dp)
                        .align(Alignment.CenterStart)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // 상단 메시지 (폰트 Medium, 크기 28, 색상 검은색, 위치 X:149, Y:71.73)
                Text(
                    text = "약관 동의",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 149.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }
    ) { padding ->
        // 전체 화면을 감싸는 컨테이너 (위치 오프셋 및 레이아웃 절대 정렬)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 🎯 1. 선 (검은색, 위치 X:15, Y:141, 외곽선 굵기 1)
            // Scaffold TopBar 바로 하단 오프셋에 맞춰 정렬 배치
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp) // 좌우 X:15 규격 확보
                    .padding(top = 16.dp), // 상단 Y:141 규격 매핑 타겟팅
                thickness = 1.dp,
                color = Color.Black
            )

            // 내용 콘텐츠 영역
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 46.dp) // 구분선 하단으로 콘텐츠 위치 시작 오프셋 조절
            ) {
                // 🎯 2. 이용약관 / 개인정보 글씨 (폰트 Medium, 크기 14, 위치 X:27, Y:171 타겟팅)
                Text(
                    text = title,
                    fontSize = 14.sp, // 크기 14 반영
                    fontWeight = FontWeight.Medium, // 폰트 Medium 반영
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 27.dp, bottom = 18.dp) // X:27 오프셋 반영
                )

                // 🎯 3. 상세내용 보여주는 네모칸 (위치 X:27, 크기 343*486, 모서리 변경 X, 외곽선 1)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 27.dp) // 좌우 X:27을 통한 가로폭 균형 맞춤
                        .fillMaxWidth()
                        .height(486.dp) // 높이 규격 486.dp 고정
                        .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp)) // 외곽선 두께 1, 모서리 원본 유지
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = termContent, // 👈 위에서 매핑된 각기 다른 진짜 약관 내용 출력
                            fontSize = 14.sp,
                            color = Color.Black,
                            lineHeight = 20.sp
                        )
                    }
                }

                // 하단 컴포넌트 안착 안정 보정용 유연 여백
                Spacer(modifier = Modifier.weight(1f))

                // 🎯 4. 하단 다음 버튼 (크기 345*63, 위치 X:24 배치, 색상 99DE81, 모서리 5, 외곽선 X)
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp) // 가로폭 345 충족을 위한 양옆 패딩 24
                        .height(63.dp), // 높이 63 규격 고정
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99DE81)),
                    shape = RoundedCornerShape(5.dp),
                    elevation = null
                ) {
                    Text(
                        text = "다음",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}