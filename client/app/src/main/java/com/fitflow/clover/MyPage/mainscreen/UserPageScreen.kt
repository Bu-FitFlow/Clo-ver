package com.fitflow.clover.mypage.mainscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.R

@Composable
fun UserPageScreen() {

    val context = LocalContext.current

    var mExpanded by remember { mutableStateOf(false) }

    var showBlockDialog by remember { mutableStateOf(false) }
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
            //UserPageScreenTopBar()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)               // 세로 높이 57dp 적용
                    .background(Color(0x3399DE81)) // 99DE81 색상으로 배경 채우기
            ) {
                // 2. 이 아이콘은 Box의 자식이므로 CenterStart (왼쪽 중앙) 정렬이 가능합니다!
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                    contentDescription = "뒤로가기 아이콘",
                    modifier = Modifier
                        .padding(start=16.dp)
                        .size(28.dp)
                        .align(Alignment.CenterStart) // 🔥 Box 내부 정렬 규칙 적용

                )
                Text(
                    text = "프로필",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd) // 👈 이 상자 자체를 상단바 오른쪽 끝으로 정렬!
                        .padding(end = 16.dp)       // 오른쪽 화면 벽에서 16dp 띄우기
                ){
                    // 3. 오른쪽: 새로 추가한 우측 아이콘 (CenterEnd)
                    Icon(
                        // ⚠️ 사용할 아이콘 리소스 ID로 변경해주세요 (예: R.drawable.ic_settings)
                        painter = painterResource(R.drawable.threecirclebutton_icon),
                        contentDescription = "우측 메뉴 아이콘",
                        modifier = Modifier
                            .padding(end = 16.dp) // 우측 레이아웃과의 여백 16dp
                            .size(28.dp)
                            .align(Alignment.CenterEnd)
                            .clickable { mExpanded = true },// 🔥 Box 내부 오른쪽 중앙 정렬
                        tint = Color.Unspecified
                    )

                    DropdownMenu(
                        expanded = mExpanded,
                        onDismissRequest = { mExpanded = false }, // 메뉴 바깥을 누르면 닫힘
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("채팅 하기", fontSize = 14.sp, fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.chat_circle),
                                    contentDescription = "채팅 아이콘",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )
                            },
                            onClick = {
                                mExpanded = false // 토스트 없이 메뉴만 닫기
                                // TODO: 나중에 여기에 채팅 화면 이동 로직(navController.navigate)을 넣으시면 됩니다!
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("신고 하기", fontSize = 14.sp, fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.stop_sign), // ⚠️ 실제 아이콘 파일명으로 맞추기!
                                    contentDescription = "신고 아이콘",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )
                            },
                            onClick = {
                                mExpanded = false // 토스트 없이 메뉴만 닫기
                                // TODO: 나중에 여기에 신고 화면 띄우는 로직을 넣으시면 됩니다!
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("차단 하기", fontSize = 14.sp, fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.bell_off), // ⚠️ 실제 아이콘 파일명으로 맞추기!
                                    contentDescription = "차단 아이콘",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )
                            },
                            onClick = {
                                mExpanded = false       // 메뉴 창 닫고
                                showBlockDialog = true  // 차단 확인 팝업창 켜기!
                            }
                        )
                    }
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

            // 🔥 여기서부터 새로 추가할 '나의 클로버' & 게이지 바 영역
            // ----------------------------------------------------
            Spacer(modifier = Modifier.height(16.dp))

            // 글자와 새싹 이미지를 담는 Column (전체 가로 여백을 선과 맞춤)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
            ) {
                // [위쪽] '나의 클로버' 텍스트와 새싹 이미지
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart // 세로 중앙 정렬
                ) {
                    Text(
                        text = "클로버",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // 🌿 새싹 이미지 (리소스 ID는 적절히 변경해주세요!)
                    Icon(
                        painter = painterResource(R.drawable.seed_icon2), // ⚠️ 실제 새싹 이미지 리소스로 변경
                        contentDescription = "새싹",
                        modifier = Modifier
                            .size(70.dp) // 크기 조절
                            .align(Alignment.Center),
                        tint = Color.Unspecified // 본래 이미지 색상 유지
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // [아래쪽] 게이지 바 (둥근 선 형태)
                // Jetpack Compose의 LinearProgressIndicator나 커스텀 Box로 구현 가능합니다.
                // 여기서는 시안과 비슷하게 테두리가 있는 커스텀 Box로 구현했습니다.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(Color.White, shape = androidx.compose.foundation.shape.CircleShape)
                        .border(1.dp, Color.Black, shape = androidx.compose.foundation.shape.CircleShape)
                        .padding(2.dp) // 테두리와 내부 게이지 사이 여백
                ) {
                    // 초록색 진행률 표시 바 (예: 35% 채워짐)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.35f) // 🔥 0.0f ~ 1.0f 사이로 채워지는 양 조절 (0.35 = 35%)
                            .background(Color(0xFF99DE81), shape = androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // [아래쪽] 다음 구분선 (시안에 있는 게이지 아래 선)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
                    .height(1.dp)
                    .background(Color.Black)
            )

            // 🔥 여기서부터 새로 추가할 '판매 물품 내역' 영역

            Spacer(modifier = Modifier.height(16.dp))

            // [타이틀 영역] '내글 보기' 텍스트와 우측 화살표(>)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // 양 끝으로 배치
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "판매 물품",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // 우측 화살표 아이콘 (시안의 > 모양)
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855), // ⚠️ 가지고 계신 화살표 아이콘(또는 뒤로가기를 회전) 리소스로 변경
                    contentDescription = "더보기",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(180f),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // [리스트 영역] 반복문을 사용해 시안의 사각형+선 세트를 4개 생성
            // 나중에 실제 데이터를 넣을 때는 repeat(4) 대신 리스트 데이터를 넣으면 됩니다.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
            ) {
                repeat(4) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp), // 아이템 간의 위아래 간격
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 시안에 있던 정사각형 이미지 박스
                        Box(
                            modifier = Modifier
                                .size(50.dp) // 정사각형 크기
                                .border(1.dp, Color.Black) // 검은색 테두리
                                .background(Color.White)
                        )

                        // 💡 만약 글씨를 넣고 싶다면 여기에 Spacer와 Text를 추가하면 됩니다.
                        // Spacer(modifier = Modifier.width(16.dp))
                        // Text(text = "물품 제목 $index", fontSize = 14.sp)
                    }

                    // 아이템 사이사이마다 들어가는 회색 구분선 (마지막 아이템 밑에는 선을 안 그리기 조건문 추가)
                    if (index < 3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.LightGray) // 연한 회색 선
                        )
                    }
                }
            }
        }
    }

    // 🎯 [팝업 코드 배치] 상태 변수가 true가 되면 화면에AlertDialog를 띄웁니다.
    if (showBlockDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDialog = false }, // 바깥 영역을 누르면 닫힘
            title = {
                Text(
                    text = "사용자 차단",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = "이 사용자를 차단하시겠습니까?\n차단 시 해당 사용자의 글과 채팅이 더 이상 보이지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBlockDialog = false
                        Toast.makeText(context, "차단이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        /* TODO: 실제 차단 서버 통신 로직 및 화면 새로고침 수행 */
                    }
                ) {
                    Text("차단", color = Color.Red) // 경고의 의미로 빨간색 처리
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBlockDialog = false }
                ) {
                    Text("취소", color = Color.Black)
                }
            }
        )
    }
}


/*
@Composable
fun UserPageScreenTopBar() {
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
fun UserPageScreenPreview() {

    UserPageScreen()
}
