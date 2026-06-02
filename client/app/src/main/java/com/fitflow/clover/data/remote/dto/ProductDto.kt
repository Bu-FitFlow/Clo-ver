package com.fitflow.clover.data.remote.dto

import com.fitflow.clover.domain.modal.ProductDetailModel
import com.fitflow.clover.domain.modal.ProductImageModel
import com.fitflow.clover.domain.modal.ProductSummaryModel
import com.fitflow.clover.domain.modal.WishlistModel
import com.google.gson.annotations.SerializedName

data class ProductSummaryResponse(
    @SerializedName("product_id")
    val productId: Long? = null,

    @SerializedName("seller_id")
    val sellerId: Long? = null,

    @SerializedName("category_id")
    val categoryId: Long? = null,

    @SerializedName("color_id")
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

    @SerializedName("trading_area")
    val tradingArea: String? = null,

    @SerializedName("recommended_type")
    val recommendedType: String? = null,

    @SerializedName("post_status")
    val postStatus: String? = null,

    @SerializedName("view_count")
    val viewCount: Int? = null,

    @SerializedName("wishlist_count")
    val wishlistCount: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("thumbnail_image")
    val thumbnailImage: ProductImageResponse? = null
)

data class ProductDetailResponse(
    @SerializedName("product_id")
    val productId: Long? = null,

    @SerializedName("seller_id")
    val sellerId: Long? = null,

    @SerializedName("category_id")
    val categoryId: Long? = null,

    @SerializedName("color_id")
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

    @SerializedName("trading_area")
    val tradingArea: String? = null,

    @SerializedName("recommended_type")
    val recommendedType: String? = null,

    @SerializedName("post_status")
    val postStatus: String? = null,

    @SerializedName("view_count")
    val viewCount: Int? = null,

    @SerializedName("wishlist_count")
    val wishlistCount: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("images")
    val images: List<ProductImageResponse>? = null,

    @SerializedName("is_wishlisted")
    val isWishlisted: Boolean? = null
)

data class ProductImageResponse(
    @SerializedName("image_id")
    val imageId: Long? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("reference_type")
    val referenceType: String? = null,

    @SerializedName("reference_id")
    val referenceId: Long? = null,

    @SerializedName("sort_order")
    val sortOrder: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)

data class WishlistResponse(
    @SerializedName("wishlist_id")
    val wishlistId: Long? = null,

    @SerializedName("member_id")
    val memberId: Long? = null,

    @SerializedName("product_id")
    val productId: Long? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)

fun ProductSummaryResponse.toDomain(): ProductSummaryModel {
    return ProductSummaryModel(
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
        thumbnailImageUrl = thumbnailImage?.imageUrl
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
            ?.filter { image ->
                image.referenceType == null || image.referenceType == "PRODUCT"
            }
            ?.sortedBy { image ->
                image.sortOrder ?: 0
            }
            ?.map { image ->
                image.toDomain()
            }
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

fun WishlistResponse.toDomain(): WishlistModel {
    return WishlistModel(
        wishlistId = wishlistId ?: 0L,
        memberId = memberId ?: 0L,
        productId = productId ?: 0L,
        createdAt = createdAt.orEmpty()
    )
}