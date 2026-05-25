package com.fitflow.clover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fitflow.clover.presentation.community.CommunityListScreen
import com.fitflow.clover.presentation.product.ProductListScreen
import com.fitflow.clover.presentation.product.ProductEditScreen
import com.fitflow.clover.presentation.trade.TradeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 테스트할 화면 주석 바꿔가며 확인
            CommunityListScreen()
//            ProductListScreen()
//            ProductEditScreen()
//            TradeScreen()
        }
    }
}