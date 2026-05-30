package com.fitflow.clover.core.navigation

sealed class ScreenRoute(val route: String) {
    data object Splash : ScreenRoute("splash")
    data object Auth : ScreenRoute("auth")
    data object Main : ScreenRoute("main")
    data object Home : ScreenRoute("home")

    data object ProductList : ScreenRoute("product_list")
    data object ProductEdit : ScreenRoute("product_edit")
    data object ProductDetail : ScreenRoute("product_detail/{productId}") {
        fun createRoute(productId: Long): String = "product_detail/$productId"
    }

    data object CommunityList : ScreenRoute("community_list")
    data object CommunityWrite : ScreenRoute("community_write")
    data object CommunityDetail : ScreenRoute("community_detail/{postId}") {
        fun createRoute(postId: Long): String = "community_detail/$postId"
    }
    data object CommunityEdit : ScreenRoute("community_edit/{postId}") {
        fun createRoute(postId: Long): String = "community_edit/$postId"
    }

    data object Chat : ScreenRoute("chat")
    data object MyPage : ScreenRoute("my_page")
}