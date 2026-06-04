package com.fitflow.clover.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// 🌟 다크 컬러셋을 아예 지워버리고, 둘 다 라이트 컬러셋으로 통일합니다.
private val LightColorScheme = lightColorScheme()

@Composable
fun CloverTheme(
    // 🌟 시스템이 다크 모드이든 아니든 상관없이 무조건 false(라이트 모드)로 강제 지정합니다!
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    // 이제 언제나 LightColorScheme만 선택됩니다.
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}