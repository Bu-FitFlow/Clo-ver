package com.fitflow.clover.core.navigation

sealed class ScreenRoute {

    object BodyAnalysis : ScreenRoute()

    object PersonalColorQuestion : ScreenRoute()

    object PersonalColorResult : ScreenRoute()

    object PersonalColorRetry : ScreenRoute()

    object Main : ScreenRoute()

    data class ProductList(
        val recommendedType: String? = null
    ) : ScreenRoute()

    data class ProductDetail(
        val productId: Long
    ) : ScreenRoute()

    object Notification : ScreenRoute()

    object Chat : ScreenRoute()

    data class ChatRoom(
        val productId: Long? = null,
        val sellerId: Long? = null,
        val chatRoomId: Long? = null
    ) : ScreenRoute()

    object TradePost : ScreenRoute()

    object Community : ScreenRoute()

    object CommunityWrite : ScreenRoute()

    object MyPage : ScreenRoute()

    object Sale : ScreenRoute()

    data class Report(
        val targetType: String = "PRODUCT",
        val targetId: Long = 0L
    ) : ScreenRoute()

    data class SellerProfile(
        val sellerId: Long = 0L
    ) : ScreenRoute()

    object CarbonPoint : ScreenRoute()
}