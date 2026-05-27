package com.fitflow.clover.presentation.product

import com.fitflow.clover.domain.modal.ProductDetailModel
import com.fitflow.clover.domain.modal.ProductSummaryModel

data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<ProductSummaryModel> = emptyList(),
    val errorMessage: String? = null
)

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: ProductDetailModel? = null,
    val errorMessage: String? = null
)

data class WishlistUiState(
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

data class ProductEditFormState(
    val name: String = "",
    val price: String = "",
    val content: String = "",
    val size: String = "",
    val grade: String = "",
    val tradingArea: String = "",
    val recommendedType: String = "",
    val postStatus: String = "ACTIVE",
    val categoryId: String = "",
    val colorId: String = ""
)