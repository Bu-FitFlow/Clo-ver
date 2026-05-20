package com.fitflow.clover.presentation.main

import androidx.compose.runtime.Composable

@Composable
fun MainScreen(
    onClickLogo: () -> Unit = {},
    onClickNotification: () -> Unit = {},
    onClickChat: () -> Unit = {},
    onClickTradePost: () -> Unit = {},
    onClickCommunity: () -> Unit = {},
    onClickMyPage: () -> Unit = {},
    onClickSale: () -> Unit = {},
    onClickWrite: () -> Unit = {},
    onClickProductMore: () -> Unit = {},
    onClickProductDetail: () -> Unit = {},
    onClickCommunityMore: () -> Unit = {},
    onClickCarbonBanner: () -> Unit = {}
) {
    HomeScreen(
        onClickLogo = onClickLogo,
        onClickNotification = onClickNotification,
        onClickChat = onClickChat,
        onClickTradePost = onClickTradePost,
        onClickCommunity = onClickCommunity,
        onClickMyPage = onClickMyPage,
        onClickSale = onClickSale,
        onClickWrite = onClickWrite,
        onClickProductMore = onClickProductMore,
        onClickProductDetail = onClickProductDetail,
        onClickCommunityMore = onClickCommunityMore,
        onClickCarbonBanner = onClickCarbonBanner
    )
}