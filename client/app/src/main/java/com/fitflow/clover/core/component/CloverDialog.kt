package com.fitflow.clover.core.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun LogoutDialog(
    onDismissRequest: () -> Unit, // 모달 닫기 (아니요 클릭 시)
    onConfirm: () -> Unit        // 로그아웃 실행 (예 클릭 시)
) {
    Dialog(onDismissRequest = onDismissRequest) {
        // 모달의 전체적인 상자 디자인
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // 1. 타이틀 영역
                Text(
                    text = "로그아웃",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 2. 질문 영역
                Text(
                    text = "로그아웃 하시겠습니까?",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 3. 버튼 영역 (예/아니요)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // '예' 버튼: Clo-ver 테마색 적용
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .width(100.dp)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "예", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // '아니요' 버튼: 강조되지 않는 텍스트 스타일
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.height(45.dp)
                    ) {
                        Text(text = "아니요", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawalDialog(
    onDismissRequest: () -> Unit, // 모달 바깥을 누르거나 취소할 때
    onConfirm: () -> Unit        // '예'를 눌렀을 때
) {
    Dialog(onDismissRequest = onDismissRequest) {
        // 모달창의 흰색 배경 상자
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // 1. 타이틀: 탈퇴하기
                Text(
                    text = "탈퇴하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 2. 본문 메시지: 정말 탈퇴 하시겠습니까?
                Text(
                    text = "정말 탈퇴 하시겠습니까?",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 3. 하단 버튼 영역 (예 / 아니요)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // '예' 버튼 (초록색 배경)
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .width(100.dp)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "예", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // '아니요' 버튼 (텍스트만)
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.height(45.dp)
                    ) {
                        Text(text = "아니요", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}


@Composable
fun DelectDialog(
    onDismissRequest: () -> Unit, // 모달 닫기 (아니요 클릭 시)
    onConfirm: () -> Unit        // 실제 삭제 로직 실행 (예 클릭 시)
) {
    // ⭐ 토스트 메시지를 띄우기 위해 현재 화면의 Context를 가져옵니다.
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // 1. 타이틀 영역
                Text(
                    text = "삭제하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 2. 질문 영역 (게시글/판매글에 맞게 조금 더 명확하게 다듬었습니다)
                Text(
                    text = "게시글을 삭제 하시겠습니까?",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 3. 버튼 영역 (예/아니요)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // '예' 버튼: Clo-ver 테마색 적용 및 토스트 메시지 출력
                    Button(
                        onClick = {
                            // ① 부모 컴포저블에서 넘겨준 진짜 삭제 로직(서버 통신이나 네비게이션) 실행
                            onConfirm()

                            // ② 화면에 "삭제가 되었습니다" 토스트 메시지 띄우기
                            Toast.makeText(context, "삭제가 되었습니다.", Toast.LENGTH_SHORT).show()

                            // ③ 다이얼로그 닫기
                            onDismissRequest()
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "예", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // '아니요' 버튼: 강조되지 않는 텍스트 스타일
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.height(45.dp)
                    ) {
                        Text(text = "아니요", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun PostDetailScreen(navController: NavController) {
    // ⭐ 다이얼로그를 띄울지 말지 결정하는 상태 변수 (기본값은 false)
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            // ... 게시글 상세 내용들 (사진, 가격, 글 내용 등) ...
            Text("중고 거래 판매글 내용", modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.weight(1f))

            // 🗑️ 화면 내에 존재하는 [삭제하기] 버튼
            Button(
                onClick = { showDeleteDialog = true }, // 버튼을 누르면 팝업 상태를 true로 변경!
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("이 글 삭제하기", color = Color.White)
            }
        }
    }

    // ⭐ 상태 변수가 true가 되면 화면 위에 다이얼로그를 띄웁니다.
    if (showDeleteDialog) {
        DelectDialog(
            onDismissRequest = { showDeleteDialog = false }, // '아니요'나 바깥 누르면 다이얼로그 닫기
            onConfirm = {
                // [조원 협업 포인트]: 나중에 백엔드 담당 조원이 여기서 서버 데이터 삭제 API를 호출하면 됩니다.
                // 지금은 삭제 완료 후 기분 좋게 게시판 목록 화면으로 돌아가도록 세팅해 둘게요!
                navController.popBackStack()
            }
        )
    }
}
