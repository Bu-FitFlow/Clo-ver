package com.fitflow.clover.mypage

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyPageViewModel : ViewModel() {

    // 🎯 외부(UI 화면)에는 읽기 전용으로 노출할 상태 데이터 (기본값 0%)
    private val _cloverProgress = MutableStateFlow(0.0f)
    val cloverProgress: StateFlow<Float> = _cloverProgress

    init {
        // 화면이 켜지자마자 DB에서 유저 점수를 불러옵니다.
        loadUserCloverScoreFromDB()
    }

    private fun loadUserCloverScoreFromDB() {
        // [여기에 나중에 실제 DB 연동 코드가 들어갑니다]
        // 예시: 파이어베이스나 레트로핏으로 유저의 현재 활동 점수를 가져옴
        val userCurrentScore = 350  // DB에서 읽어온 점수 예시
        val maxScore = 1000         // 만점 기준

        // 🎯 비율 계산 (350 / 1000 = 0.35f) 후 데이터 업데이트!
        _cloverProgress.value = userCurrentScore.toFloat() / maxScore
    }
}