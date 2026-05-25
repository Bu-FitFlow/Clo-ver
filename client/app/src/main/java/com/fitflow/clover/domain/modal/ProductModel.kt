package com.fitflow.clover.domain.modal

data class ProductSummaryModel(
    val productId: Long,
    val sellerId: Long,
    val categoryId: Long,
    val colorId: Long?,
    val name: String,
    val price: Int,
    val content: String,
    val size: String,
    val grade: String,
    val tradingArea: String,
    val recommendedType: String?,
    val postStatus: String,
    val viewCount: Int,
    val wishlistCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val thumbnailImageUrl: String?
)

data class ProductDetailModel(
    val productId: Long,
    val sellerId: Long,
    val categoryId: Long,
    val colorId: Long?,
    val name: String,
    val price: Int,
    val content: String,
    val size: String,
    val grade: String,
    val tradingArea: String,
    val recommendedType: String?,
    val postStatus: String,
    val viewCount: Int,
    val wishlistCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val images: List<ProductImageModel>,
    val isWishlisted: Boolean
)

data class ProductImageModel(
    val imageId: Long,
    val imageUrl: String,
    val referenceType: String,
    val referenceId: Long,
    val sortOrder: Int,
    val createdAt: String
)

data class WishlistModel(
    val wishlistId: Long,
    val memberId: Long,
    val productId: Long,
    val createdAt: String
)