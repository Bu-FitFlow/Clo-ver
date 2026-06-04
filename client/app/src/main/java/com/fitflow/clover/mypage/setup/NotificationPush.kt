package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun NotificationPush(navController: NavController) {

    // 💡 각 토글 버튼들의 On/Off 상태를 기억하는 변수들
    var isAllNotificationsEnabled by remember { mutableStateOf(false) }
    var isCommentNotificationEnabled by remember { mutableStateOf(false) }
    var isChatNotificationEnabled by remember { mutableStateOf(false) }
    var isEventNotificationEnabled by remember { mutableStateOf(false) }
    var isNightNotificationEnabled by remember { mutableStateOf(false) }

    // 스크롤이 가능하도록 설정
    val scrollState = rememberScrollState()

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
            PushTopBar()

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
                                // 백스택의 가장 위 화면을 날려서 이전 화면(MyPageSetup)으로 이동시킵니다.
                                navController.popBackStack()
                            }
                    )

                    Text(
                        text = "푸쉬 알림 설정",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // 위아래 간격 띄우기
                }

                // 2. 타이틀 아래의 모든 콘텐츠 영역 (양옆 20.dp 배치)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp), // 👈 여기서 전체적으로 양옆을 20.dp 띄워줍니다!
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // 알림 설정 대분류
                    SettingSectionHeader(text = "알림 설정")
                    NotificationToggleItem(
                        title = "푸시 알림 전체 동의",
                        checked = isAllNotificationsEnabled,
                        onCheckedChange = { isAllNotificationsEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 활동 알림 대분류
                    SettingSectionHeader(text = "활동 알림")
                    NotificationToggleItem(
                        title = "댓글 알림",
                        subtitle = "내 글에 댓글이 달리면 알림",
                        checked = isCommentNotificationEnabled,
                        onCheckedChange = { isCommentNotificationEnabled = it }
                    )
                    NotificationToggleItem(
                        title = "채팅 알림",
                        subtitle = "채팅 메시지 알림",
                        checked = isChatNotificationEnabled,
                        onCheckedChange = { isChatNotificationEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 기타 알림 대분류
                    SettingSectionHeader(text = "기타 알림")
                    NotificationToggleItem(
                        title = "이벤트 및 프로모션 알림",
                        checked = isEventNotificationEnabled,
                        onCheckedChange = { isEventNotificationEnabled = it }
                    )
                    NotificationToggleItem(
                        title = "야간 수신 동의",
                        checked = isNightNotificationEnabled,
                        onCheckedChange = { isNightNotificationEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun PushTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(57.dp) // 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채움
    ) {
    }
}

@Composable
fun NotificationToggleItem(title: String,
                           subtitle: String? = null, // 서브타이틀은 없을 수도 있으므로 null 허용 및 기본값 지정
                           checked: Boolean,
                           onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // 텍스트는 왼쪽, 토글은 오른쪽에 배치
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 텍스트 영역 (제목 + 설명)
        Column(
            modifier = Modifier.weight(1f) // 스위치가 밀리지 않도록 공간 확보
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
            // 서브타이틀이 주어졌을 때만 하단에 텍스트를 그림
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // 오른쪽 토글 스위치 영역
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,          // 켜졌을 때 동그라미 색상
                checkedTrackColor = Color(0xFF4CAF50),     // 켜졌을 때 바탕색 (예시: 초록색)
                uncheckedThumbColor = Color.LightGray,     // 꺼졌을 때 동그라미 색상
                uncheckedTrackColor = Color(0xFFE0E0E0)    // 꺼졌을 때 바탕색
            )
        )
    }
}




@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun NotificationPushPreview() {
    // 진짜 화면 불러오기
    NotificationPush(navController = rememberNavController())
}