package com.fitflow.clover.core.navigation

sealed class ScreenRoute {
    object BodyAnalysis : ScreenRoute()
    object PersonalColorQuestion : ScreenRoute()
    object PersonalColorResult : ScreenRoute()
    object PersonalColorRetry : ScreenRoute()

    object Main : ScreenRoute()
    object ProductDetail : ScreenRoute()
    object ProductList : ScreenRoute()

    object Notification : ScreenRoute()
    object Chat : ScreenRoute()
    object ChatRoom : ScreenRoute()
    object TradePost : ScreenRoute()
    object Community : ScreenRoute()
    object CommunityWrite : ScreenRoute()
    object MyPage : ScreenRoute()
    object Sale : ScreenRoute()
    object Report : ScreenRoute()
    object SellerProfile : ScreenRoute()
    object CarbonPoint : ScreenRoute()
}