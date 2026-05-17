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