package com.fitflow.clover.data.remote.api

import com.fitflow.clover.data.remote.dto.ProductDetailResponse
import com.fitflow.clover.data.remote.dto.ProductSummaryResponse
import com.fitflow.clover.data.remote.dto.WishlistResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("api/products/recent")
    suspend fun getRecentProducts(
        @Query("size") size: Int = 9
    ): List<ProductSummaryResponse>

    @GET("api/products/recommend")
    suspend fun getBodyRecommendedProducts(
        @Query("recommended_type") recommendedType: String,
        @Query("size") size: Int = 9
    ): List<ProductSummaryResponse>

    @GET("api/products/{product_id}")
    suspend fun getProductDetail(
        @Path("product_id") productId: Long
    ): ProductDetailResponse

    @POST("api/wishlist/products/{product_id}")
    suspend fun addWishlist(
        @Path("product_id") productId: Long
    ): WishlistResponse

    @DELETE("api/wishlist/products/{product_id}")
    suspend fun removeWishlist(
        @Path("product_id") productId: Long
    ): WishlistResponse
}