package com.fitflow.clover.presentation.splash

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.R
import kotlinx.coroutines.delay

// 1. 스플래시 화면 (기존 유지)
@Composable
fun SplashScreen(navController: NavController) {
    var progressValue by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(20)
            progressValue = i / 100f
        }
        navController.navigate("login") { popUpTo("splash") { inclusive = true } }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.frame_31), contentDescription = null, modifier = Modifier.size(200.dp))
            Spacer(modifier = Modifier.height(24.dp))
            LinearProgressIndicator(progress = { progressValue }, modifier = Modifier.width(200.dp), color = CloverGreen)
            Spacer(modifier = Modifier.height(16.dp))
            Text("당신의 옷장에 행운을 배달 중이에요", fontSize = 14.sp, color = Color.Gray)
        }
    }
}