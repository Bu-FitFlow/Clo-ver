package com.fitflow.clover.data.remote.dto

import com.fitflow.clover.domain.modal.ProductDetailModel
import com.fitflow.clover.domain.modal.ProductImageModel
import com.fitflow.clover.domain.modal.ProductSummaryModel
import com.google.gson.annotations.SerializedName

// ─── 목록 페이지네이션 래퍼 ──────────────────────────────

data class ProductPageResponse(
    @SerializedName("content")
    val content: List<ProductSummaryResponse>? = null,

    @SerializedName("last")
    val last: Boolean? = null,

    @SerializedName("number")
    val number: Int? = null,

    @SerializedName("size")
    val size: Int? = null
)

// ─── 상품 목록 아이템 ─────────────────────────────────────

data class ProductSummaryResponse(
    @SerializedName("productId")
    val productId: Long? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("price")
    val price: Int? = null,

    @SerializedName("tradingArea")
    val tradingArea: String? = null,

    @SerializedName("postStatus")
    val postStatus: String? = null,

    @SerializedName("thumbnailImageUrl")
    val thumbnailImageUrl: String? = null,

    @SerializedName("wishlistCount")
    val wishlistCount: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
)

// ─── 상품 상세 ────────────────────────────────────────────

data class ProductDetailResponse(
    @SerializedName("productId")
    val productId: Long? = null,

    @SerializedName("sellerId")
    val sellerId: Long? = null,

    @SerializedName("categoryId")
    val categoryId: Long? = null,

    @SerializedName("colorId")
    val colorId: Long? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("price")
    val price: Int? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("size")
    val size: String? = null,

    @SerializedName("grade")
    val grade: String? = null,

    @SerializedName("tradingArea")
    val tradingArea: String? = null,

    @SerializedName("recommendedType")
    val recommendedType: String? = null,

    @SerializedName("postStatus")
    val postStatus: String? = null,

    @SerializedName("viewCount")
    val viewCount: Int? = null,

    @SerializedName("wishlistCount")
    val wishlistCount: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("images")
    val images: List<ProductImageResponse>? = null,

    @SerializedName("isWishlisted")
    val isWishlisted: Boolean? = null
)

// ─── 이미지 ───────────────────────────────────────────────

data class ProductImageResponse(
    @SerializedName("imageId")
    val imageId: Long? = null,

    @SerializedName("imageUrl")
    val imageUrl: String? = null,

    @SerializedName("referenceType")
    val referenceType: String? = null,

    @SerializedName("referenceId")
    val referenceId: Long? = null,

    @SerializedName("sortOrder")
    val sortOrder: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
)

// ─── 상품 등록/수정 요청 ──────────────────────────────────

data class ProductRegisterRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("content")
    val content: String,

    @SerializedName("size")
    val size: String,

    @SerializedName("grade")
    val grade: String,

    @SerializedName("tradingArea")
    val tradingArea: String,

    @SerializedName("recommendedType")
    val recommendedType: String? = null,

    @SerializedName("categoryId")
    val categoryId: Long,

    @SerializedName("colorId")
    val colorId: Long? = null
)

// ─── toDomain 변환 ────────────────────────────────────────

fun ProductSummaryResponse.toDomain(): ProductSummaryModel {
    return ProductSummaryModel(
        productId = productId ?: 0L,
        sellerId = 0L,
        categoryId = 0L,
        colorId = null,
        name = name.orEmpty(),
        price = price ?: 0,
        content = "",
        size = "",
        grade = "",
        tradingArea = tradingArea.orEmpty(),
        recommendedType = null,
        postStatus = postStatus.orEmpty(),
        viewCount = 0,
        wishlistCount = wishlistCount ?: 0,
        createdAt = createdAt.orEmpty(),
        updatedAt = "",
        thumbnailImageUrl = thumbnailImageUrl
    )
}

fun ProductDetailResponse.toDomain(): ProductDetailModel {
    return ProductDetailModel(
        productId = productId ?: 0L,
        sellerId = sellerId ?: 0L,
        categoryId = categoryId ?: 0L,
        colorId = colorId,
        name = name.orEmpty(),
        price = price ?: 0,
        content = content.orEmpty(),
        size = size.orEmpty(),
        grade = grade.orEmpty(),
        tradingArea = tradingArea.orEmpty(),
        recommendedType = recommendedType,
        postStatus = postStatus.orEmpty(),
        viewCount = viewCount ?: 0,
        wishlistCount = wishlistCount ?: 0,
        createdAt = createdAt.orEmpty(),
        updatedAt = updatedAt.orEmpty(),
        images = images
            ?.sortedBy { it.sortOrder ?: 0 }
            ?.map { it.toDomain() }
            .orEmpty(),
        isWishlisted = isWishlisted ?: false
    )
}

fun ProductImageResponse.toDomain(): ProductImageModel {
    return ProductImageModel(
        imageId = imageId ?: 0L,
        imageUrl = imageUrl.orEmpty(),
        referenceType = referenceType.orEmpty(),
        referenceId = referenceId ?: 0L,
        sortOrder = sortOrder ?: 0,
        createdAt = createdAt.orEmpty()
    )
}