package com.fitflow.clover.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        delay(500)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 🎯 반응형 디자인 치트키: 기종별 높이 차이를 안전하게 방어하기 위해 Box 구조 사용
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter) // 상단 정렬을 기준으로 비율 여백을 줍니다.
                    .alpha(alphaAnim.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🎯 1. 피그마 Y: 248 위치를 기종별 화면 비율에 맞춰 유연하게 띄웁니다.
                // iPhone 14 Pro 높이(852) 대비 248dp의 비율을 적용한 여백 디자인입니다.
                Spacer(modifier = Modifier.fillMaxHeight(0.29f))

                // 🎯 2. 피그마 로고 크기 반영 (318.74 * 318.74)
                // 가로폭을 fillMaxWidth(0.8f)로 주어 화면 크기에 따라 자연스럽게 조절되게 해도 좋습니다.
                Image(
                    painter = painterResource(id = R.drawable.frame_31),
                    contentDescription = "Clover Logo",
                    modifier = Modifier.size(width = 319.dp, height = 319.dp)
                )

                // 🎯 3. 피그마 분석 결과 로고와 텍스트가 바로 맞물려 있으므로
                // 글자가 겹치지 않을 만큼의 최소한의 안전 여백(16dp)만 확보해 줍니다.
                Spacer(modifier = Modifier.height(16.dp))

                // 🎯 4. 피그마 텍스트 디자인 속성 반영
                // Regular(Normal), Size: 16sp, 가로 정렬 및 여백 반영
                Text(
                    text = stringResource(id = R.string.splash_delivery_message),
                    fontSize = 16.sp, // 피그마 규격 크기 16 반영
                    fontWeight = FontWeight.Normal, // Regular 반영
                    style = MaterialTheme.typography.bodyLarge, // 공통 타이포 규격 매핑
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 32.dp) // 작은 폰에서 팅기지 않게 가로 패딩 확보
                )
            }
        }
    }
}