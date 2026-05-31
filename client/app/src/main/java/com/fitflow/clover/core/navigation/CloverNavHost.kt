package com.fitflow.clover.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.presentation.chat.ChatScreen
import com.fitflow.clover.presentation.chat.ChatViewModel
import com.fitflow.clover.presentation.diagnosis.BodyAnalysisScreen
import com.fitflow.clover.presentation.diagnosis.DiagnosisViewModel
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultUiModel
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorRetryScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorScreen
import com.fitflow.clover.presentation.main.MainScreen
import com.fitflow.clover.presentation.product.ProductDetailScreen
import com.fitflow.clover.presentation.product.ProductListScreen

@Composable
fun CloverNavHost() {
    val currentScreen = remember {
        mutableStateOf<ScreenRoute>(ScreenRoute.BodyAnalysis)
    }

    val diagnosisViewModel = remember {
        DiagnosisViewModel()
    }

    val chatViewModel = remember {
        ChatViewModel()
    }

    fun currentBodyType(): String {
        return diagnosisViewModel.uiState.value.bodyResult?.bodyType
            ?.trim()
            ?.takeIf { bodyType -> bodyType.isNotBlank() }
            ?: "BALANCED"
    }

    fun currentUserDisplayName(): String {
        return diagnosisViewModel.uiState.value.userDisplayName
            .trim()
            .takeIf { displayName -> displayName.isNotBlank() }
            ?: "사용자"
    }

    fun fallbackPersonalColorResult(): PersonalColorResultUiModel {
        val userName = currentUserDisplayName()

        return PersonalColorResultUiModel(
            personalColor = "WINTER_COOL",
            resultTitle = "${userName}님은\n겨울 [쿨톤] 계열이\n잘 어울리는 타입이에요!",
            resultRecommend = "선명한 색감, 차가운 톤, 대비감이 있는 스타일이 잘 어울려요."
        )
    }

    when (val screen = currentScreen.value) {
        ScreenRoute.BodyAnalysis -> {
            BodyAnalysisScreen(
                viewModel = diagnosisViewModel,
                onMoveToPersonalColor = {
                    currentScreen.value = ScreenRoute.PersonalColorQuestion
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.PersonalColorQuestion
                }
            )
        }

        ScreenRoute.PersonalColorQuestion -> {
            PersonalColorScreen(
                viewModel = diagnosisViewModel,
                onBack = {
                    currentScreen.value = ScreenRoute.BodyAnalysis
                },
                onMoveToResult = {
                    currentScreen.value = ScreenRoute.PersonalColorResult
                },
                onMoveToRetry = {
                    currentScreen.value = ScreenRoute.PersonalColorRetry
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorResult -> {
            PersonalColorResultScreen(
                result = diagnosisViewModel.uiState.value.personalColorResult
                    ?: fallbackPersonalColorResult(),
                userDisplayName = currentUserDisplayName(),
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorRetry -> {
            PersonalColorRetryScreen(
                onRetry = {
                    diagnosisViewModel.resetPersonalColorPhoto()
                    currentScreen.value = ScreenRoute.PersonalColorQuestion
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Main -> {
            MainScreen(
                bodyType = currentBodyType(),
                onClickLogo = {
                    currentScreen.value = ScreenRoute.Main
                },
                onClickNotification = {
                    currentScreen.value = ScreenRoute.Notification
                },
                onClickChat = {
                    chatViewModel.backToChatList()
                    currentScreen.value = ScreenRoute.Chat
                },
                onClickTradePost = {
                    currentScreen.value = ScreenRoute.TradePost
                },
                onClickCommunity = {
                    currentScreen.value = ScreenRoute.Community
                },
                onClickMyPage = {
                    currentScreen.value = ScreenRoute.MyPage
                },
                onClickSale = {
                    currentScreen.value = ScreenRoute.Sale
                },
                onClickWrite = {
                    currentScreen.value = ScreenRoute.CommunityWrite
                },
                onClickProductMore = {
                    currentScreen.value = ScreenRoute.ProductList(
                        recommendedType = null
                    )
                },
                onClickBodyProductMore = {
                    currentScreen.value = ScreenRoute.ProductList(
                        recommendedType = currentBodyType()
                    )
                },
                onClickProductDetail = { productId ->
                    currentScreen.value = ScreenRoute.ProductDetail(
                        productId = productId
                    )
                },
                onClickCommunityMore = {
                    currentScreen.value = ScreenRoute.Community
                },
                onClickCarbonBanner = {
                    currentScreen.value = ScreenRoute.CarbonPoint
                }
            )
        }

        is ScreenRoute.ProductList -> {
            ProductListScreen(
                recommendedType = screen.recommendedType,
                onBack = {
                    currentScreen.value = ScreenRoute.Main
                },
                onClickProduct = { productId ->
                    currentScreen.value = ScreenRoute.ProductDetail(
                        productId = productId
                    )
                }
            )
        }

        is ScreenRoute.ProductDetail -> {
            ProductDetailScreen(
                productId = screen.productId,
                onBack = {
                    currentScreen.value = ScreenRoute.Main
                },
                onOpenChat = { productId, sellerId ->
                    chatViewModel.backToChatList()

                    currentScreen.value = ScreenRoute.ChatRoom(
                        productId = productId,
                        sellerId = sellerId,
                        chatRoomId = null
                    )
                },
                onOpenSellerProfile = { sellerId ->
                    currentScreen.value = ScreenRoute.SellerProfile(
                        sellerId = sellerId
                    )
                },
                onReportProduct = { productId ->
                    currentScreen.value = ScreenRoute.Report(
                        targetType = "PRODUCT",
                        targetId = productId
                    )
                }
            )
        }

        ScreenRoute.Notification -> {
            MainPlaceholderScreen(
                title = "알림",
                description = "알림 설정 또는 알림 목록 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Chat -> {
            LaunchedEffect(Unit) {
                chatViewModel.backToChatList()
            }

            ChatScreen(
                viewModel = chatViewModel,
                openProductChatOnStart = false,
                onBackClick = {
                    currentScreen.value = ScreenRoute.Main
                },
                onLogoClick = {
                    chatViewModel.backToChatList()
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        is ScreenRoute.ChatRoom -> {
            val hasProductChatArgs = screen.productId != null && screen.sellerId != null

            ChatScreen(
                viewModel = chatViewModel,
                openProductChatOnStart = hasProductChatArgs,
                productId = screen.productId,
                sellerId = screen.sellerId,
                onBackClick = {
                    chatViewModel.backToChatList()

                    if (screen.productId != null) {
                        currentScreen.value = ScreenRoute.ProductDetail(
                            productId = screen.productId
                        )
                    } else {
                        currentScreen.value = ScreenRoute.Main
                    }
                },
                onLogoClick = {
                    chatViewModel.backToChatList()
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.TradePost -> {
            MainPlaceholderScreen(
                title = "판매글",
                description = "내 판매글 또는 거래 게시글 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Community -> {
            MainPlaceholderScreen(
                title = "커뮤니티",
                description = "커뮤니티 목록 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.CommunityWrite -> {
            MainPlaceholderScreen(
                title = "글쓰기",
                description = "커뮤니티 글쓰기 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.MyPage -> {
            MainPlaceholderScreen(
                title = "마이 페이지",
                description = "마이페이지 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Sale -> {
            MainPlaceholderScreen(
                title = "판매",
                description = "상품 판매 등록 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        is ScreenRoute.Report -> {
            MainPlaceholderScreen(
                title = "신고",
                description = "${screen.targetType} ${screen.targetId} 신고 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        is ScreenRoute.SellerProfile -> {
            MainPlaceholderScreen(
                title = "판매자 프로필",
                description = "판매자 ID ${screen.sellerId} 프로필 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.CarbonPoint -> {
            MainPlaceholderScreen(
                title = "탄소 포인트",
                description = "탄소 포인트 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }
    }
}

@Composable
private fun MainPlaceholderScreen(
    title: String,
    description: String,
    onBackToMain: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                modifier = Modifier.padding(top = 12.dp),
                color = Color(0xFF666666),
                fontSize = 15.sp
            )

            Text(
                text = "메인으로 돌아가기",
                modifier = Modifier
                    .padding(top = 28.dp)
                    .clickable {
                        onBackToMain()
                    },
                color = Color(0xFF2F68FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}