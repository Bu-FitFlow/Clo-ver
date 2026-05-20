package com.fitflow.clover.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // 1. 화면 전체 요소의 불투명도(Alpha)를 제어할 애니메이션 상태 정의
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 2. 0.8초(800ms) 동안 서서히 나타나는(Fade-In) 애니메이션 실행
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )

        // 3. 사용자가 브랜드 이미지를 부드럽게 인지할 수 있도록 0.5초 대기
        delay(500)

        // 4. 로그인 화면으로 이동 및 스플래시 화면을 백스택에서 완전히 제거
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        // 애니메이션 알파 값을 Column에 적용하여 내부 컴포넌트들이 동시에 서서히 나타나도록 설정
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alphaAnim.value), // 서서히 나타나는 효과 적용
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = "Clover Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // progress 파라미터를 비워두면(무한 루프 형태) 하드코딩 없이
            // 시스템이 알아서 부드럽게 흐르는 로딩 바 애니메이션을 그려줍니다.
            LinearProgressIndicator(
                modifier = Modifier.width(200.dp),
                color = CloverGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "당신의 옷장에 행운을 배달 중이에요",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}