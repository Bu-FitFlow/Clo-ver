package com.fitflow.clover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.fitflow.clover.mypage.mainscreen.MyWriting
import com.fitflow.clover.mypage.mainscreen.UserPageScreen
import com.fitflow.clover.mypage.setup.MyPageNavHost
import com.fitflow.clover.ui.theme.CloverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 2. 프로젝트 전용 테마를 씌워주어야 컴포즈 UI가 정상 작동합니다.
            CloverTheme {
                // 3. 시스템 바(상단바, 네비게이션바)와 화면이 겹쳐서 안 보이는 현상을 막아주는 안전장치
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 4. 우리가 만든 화면을 감싸고, 시스템 여백(padding)을 안전하게 부여합니다.
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // 🎯 MyPageAccountProfile() 대신 네비게이션 호스트를 넣어줍니다!
                        MyPageNavHost()
                        //UserPageScreen()

                    }
                }
            }
        }
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CloverTheme {
        Greeting("Android")
    }
}

 */