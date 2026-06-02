package com.fitflow.clover.domain.usecase

import com.fitflow.clover.domain.modal.ProductDetailModel
import com.fitflow.clover.domain.modal.ProductSummaryModel
import com.fitflow.clover.domain.repository.ProductRepository

class ProductUseCase(
    private val productRepository: ProductRepository
) {

    suspend fun getRecentProducts(
        size: Int = 9
    ): List<ProductSummaryModel> {
        return productRepository.getRecentProducts(
            size = size
        )
    }

    suspend fun getBodyRecommendedProducts(
        recommendedType: String,
        size: Int = 9
    ): List<ProductSummaryModel> {
        return productRepository.getBodyRecommendedProducts(
            recommendedType = recommendedType,
            size = size
        )
    }

    suspend fun getProductDetail(
        productId: Long
    ): ProductDetailModel {
        return productRepository.getProductDetail(
            productId = productId
        )
    }

    suspend fun addWishlist(
        productId: Long
    ): Boolean {
        return productRepository.addWishlist(
            productId = productId
        )
    }

    suspend fun removeWishlist(
        productId: Long
    ): Boolean {
        return productRepository.removeWishlist(
            productId = productId
        )
    }
}