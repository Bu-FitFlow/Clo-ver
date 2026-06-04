package com.fitflow.clover.core.navigation

import android.net.Uri
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fitflow.clover.mypage.setup.MyPageNavHost
import com.fitflow.clover.mypage.setup.NotificationPush
import com.fitflow.clover.presentation.auth.*
import com.fitflow.clover.presentation.chat.ChatScreen
import com.fitflow.clover.presentation.chat.ChatViewModel
import com.fitflow.clover.presentation.community.CommunityDetailScreen
import com.fitflow.clover.presentation.community.CommunityEditScreen
import com.fitflow.clover.presentation.community.CommunityListScreen
import com.fitflow.clover.presentation.community.CommunityViewModel
import com.fitflow.clover.presentation.community.CommunityWriteScreen
import com.fitflow.clover.presentation.diagnosis.BodyAnalysisScreen
import com.fitflow.clover.presentation.diagnosis.DiagnosisViewModel
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultUiModel
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorRetryScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorScreen
import com.fitflow.clover.presentation.main.MainScreen
import com.fitflow.clover.presentation.product.ProductDetailScreen
import com.fitflow.clover.presentation.product.ProductEditScreen
import com.fitflow.clover.presentation.product.ProductListScreen
import com.fitflow.clover.presentation.product.ProductViewModel
import com.fitflow.clover.presentation.splash.SplashScreen


@Composable
fun CloverNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    CloverNavHost(
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun CloverNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "splash"
) {
    val productViewModel: ProductViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()

    val diagnosisViewModel = remember {
        DiagnosisViewModel()
    }

    val chatViewModel = remember {
        ChatViewModel()
    }

    fun navigateSingleTop(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun popBackOrMain() {
        val popped = navController.popBackStack()

        if (!popped) {
            navController.navigate(ScreenRoute.Main.route) {
                launchSingleTop = true
            }
        }
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

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginMain(navController)
        }

        composable("join_terms") {
            JoinTerms(navController)
        }

        composable("term_detail_1") {
            TermDetailScreen(navController, "이용약관 동의(필수)")
        }

        composable("term_detail_2") {
            TermDetailScreen(navController, "개인정보 수집 및 이용동의(필수)")
        }

        composable("join_detail") {
            JoinDetail(navController)
        }

        composable("find_id_pw") {
            FindIdPw(navController)
        }

        composable("reset_password") {
            ResetPasswordScreen(navController)
        }

        composable(ScreenRoute.BodyAnalysis.route) {
            BodyAnalysisScreen(
                viewModel = diagnosisViewModel,
                onMoveToPersonalColor = {
                    navigateSingleTop(ScreenRoute.PersonalColorQuestion.route)
                },
                onMoveToMain = {
                    navigateSingleTop(ScreenRoute.PersonalColorQuestion.route)
                }
            )
        }

        composable(ScreenRoute.PersonalColorQuestion.route) {
            PersonalColorScreen(
                viewModel = diagnosisViewModel,
                onBack = {
                    navigateSingleTop(ScreenRoute.BodyAnalysis.route)
                },
                onMoveToResult = {
                    navigateSingleTop(ScreenRoute.PersonalColorResult.route)
                },
                onMoveToRetry = {
                    navigateSingleTop(ScreenRoute.PersonalColorRetry.route)
                },
                onMoveToMain = {
                    navigateSingleTop(ScreenRoute.Main.route)
                }
            )
        }

        composable(ScreenRoute.PersonalColorResult.route) {
            PersonalColorResultScreen(
                result = diagnosisViewModel.uiState.value.personalColorResult
                    ?: fallbackPersonalColorResult(),
                userDisplayName = currentUserDisplayName(),
                onMoveToMain = {
                    navController.navigate(ScreenRoute.Main.route) {
                        popUpTo(ScreenRoute.BodyAnalysis.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(ScreenRoute.PersonalColorRetry.route) {
            PersonalColorRetryScreen(
                onRetry = {
                    diagnosisViewModel.resetPersonalColorPhoto()
                    navigateSingleTop(ScreenRoute.PersonalColorQuestion.route)
                },
                onMoveToMain = {
                    navigateSingleTop(ScreenRoute.Main.route)
                }
            )
        }

        composable(ScreenRoute.Main.route) {
            MainScreen(
                bodyType = currentBodyType(),
                onClickLogo = {
                    navigateSingleTop(ScreenRoute.Main.route)
                },
                onClickNotification = {
                    navigateSingleTop("notification_push")
                },
                onClickChat = {
                    chatViewModel.backToChatList()
                    navigateSingleTop(ScreenRoute.Chat.route)
                },
                onClickTradePost = {
                    navigateSingleTop(ScreenRoute.TradePost.route)
                },
                onClickCommunity = {
                    navigateSingleTop(ScreenRoute.CommunityList.route)
                },
                onClickMyPage = {
                    navigateSingleTop(ScreenRoute.MyPage.route)
                },
                onClickSale = {
                    productViewModel.prepareRegister()
                    navigateSingleTop(ScreenRoute.ProductEdit.route)
                },
                onClickWrite = {
                    communityViewModel.resetWriteState()
                    navigateSingleTop(ScreenRoute.CommunityWrite.route)
                },
                onClickProductMore = {
                    navigateSingleTop(ScreenRoute.ProductList.route)
                },
                onClickBodyProductMore = {
                    navigateSingleTop(ScreenRoute.ProductList.route)
                },
                onClickProductDetail = { productId ->
                    navController.navigate(
                        ScreenRoute.ProductDetail.createRoute(productId)
                    )
                },
                onClickCommunityMore = {
                    navigateSingleTop(ScreenRoute.CommunityList.route)
                },
                onClickCarbonBanner = {
                    navigateSingleTop(ScreenRoute.CarbonPoint.route)
                },
                onClickSearch = {
                    navigateSingleTop("search_screen")
                }
            )
        }


        composable(ScreenRoute.ProductList.route) {
            ProductListRouteContent(
                navController = navController,
                productViewModel = productViewModel,
                communityViewModel = communityViewModel,
                onBackClick = {
                    popBackOrMain()
                }
            )
        }

        composable(
            route = ScreenRoute.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: return@composable

            ProductDetailScreen(
                productId = productId,
                productViewModel = productViewModel,
                onBack = {
                    popBackOrMain()
                },
                onClickLogo = {
                    navController.navigate(ScreenRoute.Main.route) {
                        launchSingleTop = true
                        popUpTo(ScreenRoute.Main.route) {
                            inclusive = false
                        }
                    }
                },
                onOpenChat = { selectedProductId, sellerId ->
                    navController.navigate(
                        ScreenRoute.ChatRoom.createRoute(
                            productId = selectedProductId,
                            sellerId = sellerId,
                            chatRoomId = null
                        )
                    )
                },
                onOpenSellerProfile = { sellerId ->
                    navController.navigate(
                        ScreenRoute.SellerProfile.createRoute(sellerId)
                    )
                },
                onReportProduct = { reportProductId ->
                    navController.navigate(
                        ScreenRoute.Report.createRoute(
                            targetType = "PRODUCT",
                            targetId = reportProductId
                        )
                    )
                }
            )
        }

        composable(ScreenRoute.ProductEdit.route) {
            ProductEditRouteContent(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        composable(ScreenRoute.Sale.route) {
            LaunchedEffect(Unit) {
                productViewModel.prepareRegister()
            }

            ProductEditRouteContent(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        composable(ScreenRoute.Community.route) {
            CommunityListRouteContent(
                navController = navController,
                productViewModel = productViewModel,
                communityViewModel = communityViewModel
            )
        }

        composable(ScreenRoute.CommunityList.route) {
            CommunityListRouteContent(
                navController = navController,
                productViewModel = productViewModel,
                communityViewModel = communityViewModel
            )
        }

        composable(ScreenRoute.CommunityWrite.route) {
            val writeUiState by communityViewModel.writeUiState.collectAsState()

            CommunityWriteScreen(
                uiState = writeUiState,
                onBackClick = {
                    popBackOrMain()
                },
                onTitleChange = communityViewModel::onWriteTitleChange,
                onCategorySelect = communityViewModel::onWriteCategorySelect,
                onCategoryDropdownToggle = communityViewModel::onWriteCategoryDropdownToggle,
                onContentBlocksChange = communityViewModel::onWriteContentBlocksChange,
                onCompleteClick = { imageUri: Uri? ->
                    communityViewModel.submitPost(imageUri) {
                        navController.navigate(ScreenRoute.CommunityList.route) {
                            popUpTo(ScreenRoute.CommunityList.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(
            route = ScreenRoute.CommunityDetail.route,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getLong("postId") ?: return@composable
            val detailUiState by communityViewModel.detailUiState.collectAsState()

            LaunchedEffect(postId) {
                communityViewModel.loadPostDetail(postId)
            }

            CommunityDetailScreen(
                uiState = detailUiState,
                onBackClick = {
                    popBackOrMain()
                },
                onLikeClick = communityViewModel::onLikeClick,
                onCommentInputChange = communityViewModel::onCommentInputChange,
                onCommentSubmit = communityViewModel::onCommentSubmit,
                onReplyClick = communityViewModel::onReplyClick,
                onReplyInputChange = communityViewModel::onReplyInputChange,
                onReplySubmit = communityViewModel::onReplySubmit,
                onCommentDeleteClick = communityViewModel::onCommentDeleteClick,
                onMenuClick = communityViewModel::onDetailMenuClick,
                onEditClick = {
                    navController.navigate(
                        ScreenRoute.CommunityEdit.createRoute(postId)
                    )
                },
                onDeleteClick = {
                    communityViewModel.deleteCurrentPost {
                        popBackOrMain()
                    }
                },
                onReportClick = communityViewModel::onReportClick,
                onBlockClick = communityViewModel::onBlockClick
            )
        }

        composable(
            route = ScreenRoute.CommunityEdit.route,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getLong("postId") ?: return@composable
            val editUiState by communityViewModel.editUiState.collectAsState()

            LaunchedEffect(postId) {
                communityViewModel.startEdit(postId)
            }

            CommunityEditScreen(
                uiState = editUiState,
                onBackClick = {
                    popBackOrMain()
                },
                onTitleChange = communityViewModel::onEditTitleChange,
                onCategorySelect = communityViewModel::onEditCategorySelect,
                onCategoryDropdownToggle = communityViewModel::onEditCategoryDropdownToggle,
                onContentBlocksChange = communityViewModel::onEditContentBlocksChange,
                onSubmitClick = { imageUri: Uri? ->
                    communityViewModel.submitEditPost(postId, imageUri) {
                        popBackOrMain()
                    }
                }
            )
        }

        composable(ScreenRoute.Chat.route) {
            LaunchedEffect(Unit) {
                chatViewModel.backToChatList()
            }

            ChatScreen(
                viewModel = chatViewModel,
                openProductChatOnStart = false,
                onBackClick = {
                    navigateSingleTop(ScreenRoute.Main.route)
                },
                onLogoClick = {
                    chatViewModel.backToChatList()
                    navigateSingleTop(ScreenRoute.Main.route)
                }
            )
        }

        composable(
            route = ScreenRoute.ChatRoom.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("sellerId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("chatRoomId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments
                ?.getString("productId")
                ?.toLongOrNull()

            val sellerId = backStackEntry.arguments
                ?.getString("sellerId")
                ?.toLongOrNull()

            val hasProductChatArgs = productId != null && sellerId != null

            ChatScreen(
                viewModel = chatViewModel,
                openProductChatOnStart = hasProductChatArgs,
                productId = productId,
                sellerId = sellerId,
                onBackClick = {
                    chatViewModel.backToChatList()

                    val popped = navController.popBackStack()

                    if (!popped) {
                        navigateSingleTop(ScreenRoute.Main.route)
                    }
                },
                onLogoClick = {
                    chatViewModel.backToChatList()

                    navController.navigate(ScreenRoute.Main.route) {
                        launchSingleTop = true
                        popUpTo(ScreenRoute.Main.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable("notification_push") {
            NotificationPush(
                navController = navController
            )
        }

        composable("search_screen") {
            // 다른 파일들과 구별하기 위해 질문자님의 파일 패키지명을 앞에 통째로 붙여 호출합니다.
            com.fitflow.clover.mypage.mainscreen.(
                onBackClick = {
                    popBackOrMain()
                }
            )
        }

        composable(ScreenRoute.TradePost.route) {
            MainPlaceholderScreen(
                title = "판매글",
                description = "내 판매글 또는 거래 게시글 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    navigateSingleTop(ScreenRoute.Main.route)
                }
            )
        }

        composable(ScreenRoute.MyPage.route) {
            MyPageNavHost(
                onExitMyPage = {
                    // 🎯 마이페이지 메인 화면에서 '뒤로가기'를 누르면 전체 앱의 메인 화면으로 이동!
                    navigateSingleTop(ScreenRoute.Main.route)
                }
            )
        }

        composable(
            route = ScreenRoute.Report.route,
            arguments = listOf(
                navArgument("targetType") {
                    type = NavType.StringType
                    defaultValue = "PRODUCT"
                },
                navArgument("targetId") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ) { backStackEntry ->
            val targetType = backStackEntry.arguments?.getString("targetType") ?: "PRODUCT"
            val targetId = backStackEntry.arguments
                ?.getString("targetId")
                ?.toLongOrNull()
                ?: 0L

            MainPlaceholderScreen(
                title = "신고",
                description = "$targetType $targetId 신고 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    popBackOrMain()
                }
            )
        }

        composable(
            route = ScreenRoute.SellerProfile.route,
            arguments = listOf(
                navArgument("sellerId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getLong("sellerId") ?: 0L

            MainPlaceholderScreen(
                title = "판매자 프로필",
                description = "판매자 ID $sellerId 프로필 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    popBackOrMain()
                }
            )
        }

        composable(ScreenRoute.CarbonPoint.route) {
            MainPlaceholderScreen(
                title = "탄소 포인트",
                description = "탄소 포인트 화면으로 연결될 예정입니다.",
                onBackToMain = {
                    navigateSingleTop(ScreenRoute.Main.route)
                }
            )
        }
    }
}


@Composable
private fun ProductListRouteContent(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    communityViewModel: CommunityViewModel,
    onBackClick: () -> Unit
) {
    val listUiState by productViewModel.listUiState.collectAsState()

    ProductListScreen(
        uiState = listUiState,
        onProductClick = { productId ->
            navController.navigate(
                ScreenRoute.ProductDetail.createRoute(productId)
            )
        },
        onMainCategorySelect = productViewModel::onMainCategorySelect,
        onMainCategoryExpandChange = productViewModel::onMainCategoryExpandChange,
        onSubCategorySelect = productViewModel::onSubCategorySelect,
        onSubCategoryExpandChange = productViewModel::onSubCategoryExpandChange,
        onFilterClick = productViewModel::onFilterClick,
        onProductListClick = {
            navController.navigate(ScreenRoute.ProductList.route) {
                launchSingleTop = true
            }
        },
        onCommunityClick = {
            navController.navigate(ScreenRoute.CommunityList.route) {
                launchSingleTop = true
            }
        },
        onSellClick = {
            productViewModel.prepareRegister()
            navController.navigate(ScreenRoute.ProductEdit.route)
        },
        onCommunityWriteClick = {
            communityViewModel.resetWriteState()
            navController.navigate(ScreenRoute.CommunityWrite.route)
        },
        onChatClick = {
            navController.navigate(ScreenRoute.Chat.route) {
                launchSingleTop = true
            }
        },
        onMyPageClick = {
            navController.navigate(ScreenRoute.MyPage.route) {
                launchSingleTop = true
            }
        },
        onBackClick = onBackClick
    )
}

@Composable
private fun ProductEditRouteContent(
    navController: NavHostController,
    productViewModel: ProductViewModel
) {
    val editUiState by productViewModel.editUiState.collectAsState()

    ProductEditScreen(
        uiState = editUiState,
        onBackClick = {
            navController.popBackStack()
        },
        onTitleChange = productViewModel::onEditTitleChange,
        onPriceChange = productViewModel::onEditPriceChange,
        onDescriptionChange = productViewModel::onEditDescriptionChange,
        onTradeLocationChange = productViewModel::onEditTradeLocationChange,
        onSizeChange = productViewModel::onEditSizeChange,
        onFitChange = productViewModel::onEditFitChange,
        onMainCategorySelect = productViewModel::onEditMainCategorySelect,
        onMainCategoryExpandChange = productViewModel::onEditMainCategoryExpandChange,
        onSubCategorySelect = productViewModel::onEditSubCategorySelect,
        onSubCategoryExpandChange = productViewModel::onEditSubCategoryExpandChange,
        onImageUrisChange = productViewModel::onEditImageUrisChange,
        onSubmitClick = {
            productViewModel.submitProduct {
                navController.popBackStack()
            }
        }
    )
}

@Composable
private fun CommunityListRouteContent(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    communityViewModel: CommunityViewModel
) {
    val communityListUiState by communityViewModel.listUiState.collectAsState()

    CommunityListScreen(
        uiState = communityListUiState,
        onPostClick = { postId ->
            navController.navigate(
                ScreenRoute.CommunityDetail.createRoute(postId)
            )
        },
        onWriteClick = {
            communityViewModel.resetWriteState()
            navController.navigate(ScreenRoute.CommunityWrite.route)
        },
        onCategorySelect = communityViewModel::onCategorySelect,
        onSearchQueryChange = communityViewModel::onSearchQueryChange,
        onBackClick = {
            navController.popBackStack()
        },
        onProductListClick = {
            navController.navigate(ScreenRoute.ProductList.route) {
                launchSingleTop = true
            }
        },
        onCommunityClick = {
            navController.navigate(ScreenRoute.CommunityList.route) {
                launchSingleTop = true
            }
        },
        onSellClick = {
            productViewModel.prepareRegister()
            navController.navigate(ScreenRoute.ProductEdit.route)
        },
        onChatClick = {
            navController.navigate(ScreenRoute.Chat.route) {
                launchSingleTop = true
            }
        },
        onMyPageClick = {
            navController.navigate(ScreenRoute.MyPage.route) {
                launchSingleTop = true
            }
        }
    )
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