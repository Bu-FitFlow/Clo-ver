package com.fitflow.clover.core.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fitflow.clover.presentation.community.CommunityDetailScreen
import com.fitflow.clover.presentation.community.CommunityEditScreen
import com.fitflow.clover.presentation.community.CommunityListScreen
import com.fitflow.clover.presentation.community.CommunityViewModel
import com.fitflow.clover.presentation.community.CommunityWriteScreen
import com.fitflow.clover.presentation.product.ProductEditScreen
import com.fitflow.clover.presentation.product.ProductListScreen
import com.fitflow.clover.presentation.product.ProductViewModel

@Composable
fun CloverNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = ScreenRoute.ProductList.route
) {
    val productViewModel: ProductViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ScreenRoute.ProductList.route) {
            val listUiState by productViewModel.listUiState.collectAsState()

            ProductListScreen(
                uiState = listUiState,
                onProductClick = { productId ->
                    // TODO: ProductDetailScreen 완성 후 아래 주석을 해제하세요.
                    // navController.navigate(ScreenRoute.ProductDetail.createRoute(productId))
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
                    navController.navigate(ScreenRoute.CommunityList.route) {
                        launchSingleTop = true
                    }
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
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(ScreenRoute.ProductEdit.route) {
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


        composable(ScreenRoute.CommunityList.route) {
            val communityListUiState by communityViewModel.listUiState.collectAsState()

            CommunityListScreen(
                uiState = communityListUiState,
                onPostClick = { postId ->
                    navController.navigate(ScreenRoute.CommunityDetail.createRoute(postId))
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

        composable(ScreenRoute.CommunityWrite.route) {
            val writeUiState by communityViewModel.writeUiState.collectAsState()

            CommunityWriteScreen(
                uiState = writeUiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onTitleChange = communityViewModel::onWriteTitleChange,
                onCategorySelect = communityViewModel::onWriteCategorySelect,
                onCategoryDropdownToggle = communityViewModel::onWriteCategoryDropdownToggle,
                onContentBlocksChange = communityViewModel::onWriteContentBlocksChange,
                onCompleteClick = { imageUri: Uri? ->
                    communityViewModel.submitPost(imageUri) { _ ->
                        navController.navigate(ScreenRoute.CommunityList.route) {
                            // 백스택에서 CommunityList까지 정리하고 CommunityList로 이동
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
                    navController.popBackStack()
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
                    navController.navigate(ScreenRoute.CommunityEdit.createRoute(postId))
                },
                onDeleteClick = {
                    communityViewModel.deleteCurrentPost {
                        navController.popBackStack()
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
                    navController.popBackStack()
                },
                onTitleChange = communityViewModel::onEditTitleChange,
                onCategorySelect = communityViewModel::onEditCategorySelect,
                onCategoryDropdownToggle = communityViewModel::onEditCategoryDropdownToggle,
                onContentBlocksChange = communityViewModel::onEditContentBlocksChange,
                onSubmitClick = { imageUri: Uri? ->
                    communityViewModel.submitEditPost(postId, imageUri) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(ScreenRoute.Chat.route) {            PlaceholderScreen(text = "채팅 화면은 추후 연결 예정입니다.")
        }

        composable(ScreenRoute.MyPage.route) {
            PlaceholderScreen(text = "마이페이지 화면은 추후 연결 예정입니다.")
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}