package com.fitflow.clover.presentation.report

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel : ViewModel() {

    // 와이어프레임에 노출되는 신고 사유 리스트
    val reportReasons = listOf(
        "사기 피해를 입었어요.",
        "욕설, 비방, 혐오적인 표현을 해요.",
        "물품 하자, 구매 미확정이 발생했어요.",
        "기타"
    )

    // 상태 관리 변수 (노란색 경고 유발하던 <String>, <Boolean> 생략 완료)
    private val _selectedReason = MutableStateFlow("")
    val selectedReason: StateFlow<String> = _selectedReason.asStateFlow()

    private val _reportContent = MutableStateFlow("")
    val reportContent: StateFlow<String> = _reportContent.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _temporarySelectedReason = MutableStateFlow("")
    val temporarySelectedReason: StateFlow<String> = _temporarySelectedReason.asStateFlow()

    // 🌟 [Unresolved reference 완벽 해결] 실시간으로 제출 조건이 맞는지 검사하는 함수
    fun checkSubmitEnabled(reason: String, content: String): Boolean {
        return reason.isNotEmpty() && content.isNotBlank()
    }

    // 상태 제어 비즈니스 로직 함수들
    fun setReportContent(content: String) {
        _reportContent.value = content
    }

    fun setShowBottomSheet(show: Boolean) {
        if (show) {
            _temporarySelectedReason.value = _selectedReason.value
        }
        _showBottomSheet.value = show
    }

    fun setTemporaryReason(reason: String) {
        _temporarySelectedReason.value = reason
    }

    fun confirmReason() {
        _selectedReason.value = _temporarySelectedReason.value
        _showBottomSheet.value = false
    }
}