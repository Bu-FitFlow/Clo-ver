package com.fitflow.clover.presentation.trade

import com.fitflow.clover.domain.modal.ProductSummary

// ─────────────────────────────────────────────────────────
// 판매관리 화면 상태
// ─────────────────────────────────────────────────────────
data class TradeUiState(
    val isLoading: Boolean = false,
    val isSellingTabSelected: Boolean = true,
    val sellingProducts: List<ProductSummary> = emptyList(),
    val soldProducts: List<ProductSummary> = emptyList(),
    val errorMessage: String? = null
)
