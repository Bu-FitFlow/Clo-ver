package com.fitflow.clover.presentation.product

import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary

// ─────────────────────────────────────────────────────────
// 판매글 목록 화면 상태
// ─────────────────────────────────────────────────────────
data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<ProductSummary> = emptyList(),

    // 대분류 드롭다운 (전체/상의/바지/아우터/원피스·스커트)
    val selectedMainCategory: ProductMainCategory = ProductMainCategory.ALL,
    val isMainCategoryExpanded: Boolean = false,

    // 세부 드롭다운 (대분류 선택 시 자동 연동)
    val selectedSubCategory: ProductSubCategory? = null,
    val isSubCategoryExpanded: Boolean = false,
    val subCategoryList: List<ProductSubCategory> = emptyList(),

    val isLatestOrder: Boolean = true,        // 필터 버튼 → 최신순
    val errorMessage: String? = null
)

// ─────────────────────────────────────────────────────────
// 판매관리 화면 상태
// ─────────────────────────────────────────────────────────
data class TradeUiState(
    val isLoading: Boolean = false,
    val sellingProducts: List<ProductSummary> = emptyList(),   // 판매 중 목록
    val soldProducts: List<ProductSummary> = emptyList(),      // 거래 완료 목록
    val isSellingTabSelected: Boolean = false,                 // false = 거래완료 탭
    val errorMessage: String? = null
)

