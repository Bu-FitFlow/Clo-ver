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
    val selectedMainCategory: ProductMainCategory = ProductMainCategory.ALL,
    val isMainCategoryExpanded: Boolean = false,
    val selectedSubCategory: ProductSubCategory? = null,
    val isSubCategoryExpanded: Boolean = false,
    val subCategoryList: List<ProductSubCategory> = emptyList(),
    val isLatestOrder: Boolean = true,
    val errorMessage: String? = null
)

// ─────────────────────────────────────────────────────────
// 판매관리 화면 상태
// ─────────────────────────────────────────────────────────
data class TradeUiState(
    val isLoading: Boolean = false,
    val sellingProducts: List<ProductSummary> = emptyList(),
    val soldProducts: List<ProductSummary> = emptyList(),
    val isSellingTabSelected: Boolean = true,
    val errorMessage: String? = null
)

// ─────────────────────────────────────────────────────────
// 상품 등록/수정 화면 상태
// ─────────────────────────────────────────────────────────
data class ProductEditUiState(
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,           // false = 등록, true = 수정
    val productId: Long? = null,               // 수정 시 기존 상품 ID
    val title: String = "",                    // 상품명
    val price: String = "",                    // 판매 가격
    val description: String = "",              // 상품 정보
    val tradeLocation: String = "",            // 거래 지역
    val size: String = "",                     // 사이즈
    val fit: String = "",                      // 핏 선택 사항
    val selectedMainCategory: ProductMainCategory? = null,
    val isMainCategoryExpanded: Boolean = false,
    val selectedSubCategory: ProductSubCategory? = null,
    val isSubCategoryExpanded: Boolean = false,
    val subCategoryList: List<ProductSubCategory> = emptyList(),
    val imageUris: List<String> = emptyList(), // 선택한 이미지 목록 (최대 5장)
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val errorMessage: String? = null
)