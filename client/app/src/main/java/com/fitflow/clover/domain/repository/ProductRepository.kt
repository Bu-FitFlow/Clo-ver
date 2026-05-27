package com.fitflow.clover.domain.repository

import com.fitflow.clover.domain.modal.ProductDetailModel
import com.fitflow.clover.domain.modal.ProductSummaryModel

interface ProductRepository {

    suspend fun getRecentProducts(
        size: Int = 9
    ): List<ProductSummaryModel>

    suspend fun getBodyRecommendedProducts(
        recommendedType: String,
        size: Int = 9
    ): List<ProductSummaryModel>

    suspend fun getProductDetail(
        productId: Long
    ): ProductDetailModel

    suspend fun addWishlist(
        productId: Long
    ): Boolean

    suspend fun removeWishlist(
        productId: Long
    ): Boolean
}