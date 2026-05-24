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
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorScreen
import com.fitflow.clover.presentation.main.MainScreen
import com.fitflow.clover.presentation.product.ProductDetailsScreen

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

    when (currentScreen.value) {
        ScreenRoute.BodyAnalysis -> {
            BodyAnalysisScreen(
                viewModel = diagnosisViewModel,
                onMoveToPersonalColor = {
                    currentScreen.value = ScreenRoute.PersonalColorQuestion
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorQuestion -> {
            PersonalColorScreen(
                viewModel = diagnosisViewModel,
                onMoveToResult = {
                    currentScreen.value = ScreenRoute.PersonalColorResult
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorResult -> {
            PersonalColorResultScreen(
                result = diagnosisViewModel.uiState.value.personalColorResult
                    ?: PersonalColorResultUiModel(
                        personalColor = "WINTER_COOL",
                        resultTitle = "00님은\n겨울 [쿨톤] 계열이\n잘 어울리는 타입이에요!",
                        resultRecommend = "선명한 색감, 차가운 톤, 대비감이 있는 스타일이 잘 어울려요."
                    ),
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorRetry -> {
            PersonalColorScreen(
                viewModel = diagnosisViewModel,
                onMoveToResult = {
                    currentScreen.value = ScreenRoute.PersonalColorResult
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Main -> {
            MainScreen(
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
                    currentScreen.value = ScreenRoute.ProductList
                },
                onClickProductDetail = {
                    currentScreen.value = ScreenRoute.ProductDetail
                },
                onClickCommunityMore = {
                    currentScreen.value = ScreenRoute.Community
                },
                onClickCarbonBanner = {
                    currentScreen.value = ScreenRoute.CarbonPoint
                }
            )
        }

        ScreenRoute.ProductDetail -> {
            ProductDetailsScreen(
                onBack = {
                    currentScreen.value = ScreenRoute.Main
                },
                onClickLogo = {
                    currentScreen.value = ScreenRoute.Main
                },
                onReport = {
                    currentScreen.value = ScreenRoute.Report
                },
                onOpenChat = {
                    currentScreen.value = ScreenRoute.ChatRoom
                },
                onOpenSellerProfile = {
                    currentScreen.value = ScreenRoute.SellerProfile
                },
                onHome = {
                    currentScreen.value = ScreenRoute.Main
                },
                onBoard = {
                    currentScreen.value = ScreenRoute.Community
                },
                onNotification = {
                    currentScreen.value = ScreenRoute.Notification
                },
                onMyPage = {
                    currentScreen.value = ScreenRoute.MyPage
                }
            )
        }

        ScreenRoute.ProductList -> {
            MainPlaceholderScreen(
                title = "전체 상품",
                description = "상품 전체보기 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
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
            ChatScreen(
                viewModel = chatViewModel,
                openProductChatOnStart = false,
                onBackClick = {
                    currentScreen.value = ScreenRoute.Main
                },
                onLogoClick = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.ChatRoom -> {
            ChatScreen(
                viewModel = chatViewModel,
                openProductChatOnStart = true,
                onBackClick = {
                    currentScreen.value = ScreenRoute.ProductDetail
                },
                onLogoClick = {
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

        ScreenRoute.CommunityWrite -> {
            MainPlaceholderScreen(
                title = "글쓰기",
                description = "커뮤니티 글쓰기 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Report -> {
            MainPlaceholderScreen(
                title = "신고 하기",
                description = "상품 신고 페이지로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.ProductDetail
                }
            )
        }

        ScreenRoute.SellerProfile -> {
            MainPlaceholderScreen(
                title = "프로필 보기",
                description = "판매자 프로필 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    currentScreen.value = ScreenRoute.ProductDetail
                }
            )
        }

        ScreenRoute.CarbonPoint -> {
            MainPlaceholderScreen(
                title = "탄소 포인트 제도",
                description = "탄소 포인트 제도 안내 화면으로 연결될 예정입니다.",
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
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 14.dp)
            )

            Text(
                text = "돌아가기",
                color = Color(0xFF4A9D3A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 28.dp)
                    .clickable(onClick = onBackToMain)
            )
        }
    }
}