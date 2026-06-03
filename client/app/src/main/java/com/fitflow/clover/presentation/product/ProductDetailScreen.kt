package com.fitflow.clover.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitflow.clover.domain.modal.ProductDetailModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ProductDetailScreen(
    productId: Long,
    onBack: () -> Unit,
    onOpenChat: (productId: Long, sellerId: Long) -> Unit,
    onOpenSellerProfile: (sellerId: Long) -> Unit = {},
    onReportProduct: (productId: Long) -> Unit = {}
) {
    val productViewModel = remember {
        ProductViewModel()
    }

    val detailUiState by productViewModel.detailUiState.collectAsState()
    val wishlistUiState by productViewModel.wishlistUiState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(productId) {
        productViewModel.loadProductDetail(
            productId = productId
        )
    }

    LaunchedEffect(detailUiState.errorMessage) {
        detailUiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            productViewModel.clearDetailError()
        }
    }

    LaunchedEffect(wishlistUiState.errorMessage) {
        wishlistUiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            productViewModel.clearWishlistError()
        }
    }

    val product = detailUiState.product

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            product?.let { currentProduct ->
                ProductDetailBottomBar(
                    product = currentProduct,
                    isWishlistProcessing = wishlistUiState.isProcessing,
                    onClickWishlist = {
                        productViewModel.toggleWishlist()
                    },
                    onClickChat = {
                        onOpenChat(
                            currentProduct.productId,
                            currentProduct.sellerId
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
                .padding(innerPadding)
        ) {
            when {
                detailUiState.isLoading -> {
                    ProductDetailLoadingContent(
                        onBack = onBack
                    )
                }

                product != null -> {
                    ProductDetailContent(
                        product = product,
                        onBack = onBack,
                        onOpenSellerProfile = {
                            onOpenSellerProfile(product.sellerId)
                        },
                        onReportProduct = {
                            onReportProduct(product.productId)
                        }
                    )
                }

                else -> {
                    ProductDetailErrorContent(
                        message = detailUiState.errorMessage.orEmpty(),
                        onBack = onBack,
                        onRetry = {
                            productViewModel.loadProductDetail(
                                productId = productId
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailLoadingContent(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProductDetailTopBar(
            title = "상품 상세",
            onBack = onBack,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ProductDetailErrorContent(
    message: String,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProductDetailTopBar(
            title = "상품 상세",
            onBack = onBack
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = message.ifBlank { "상품 정보를 불러오지 못했습니다." },
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF777777),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onRetry
        ) {
            Text(text = "다시 불러오기")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ProductDetailContent(
    product: ProductDetailModel,
    onBack: () -> Unit,
    onOpenSellerProfile: () -> Unit,
    onReportProduct: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ProductDetailTopBar(
                title = "상품 상세",
                onBack = onBack
            )
        }

        item {
            ProductDetailImagePager(
                images = product.images
            )
        }

        item {
            ProductInfoSection(
                product = product
            )
        }

        item {
            SellerSection(
                sellerId = product.sellerId,
                onOpenSellerProfile = onOpenSellerProfile
            )
        }

        item {
            ProductDescriptionSection(
                content = product.content
            )
        }

        item {
            ProductReportSection(
                onReportProduct = onReportProduct
            )
        }

        item {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
private fun ProductDetailTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFF1F3F5))
                .clickable {
                    onBack()
                }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )
    }
}

@Composable
private fun ProductInfoSection(
    product: ProductDetailModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = formatPrice(product.price),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF202C59)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProductInfoRow(
            label = "사이즈",
            value = product.size
        )

        ProductInfoRow(
            label = "상품 상태",
            value = product.grade
        )

        ProductInfoRow(
            label = "거래 지역",
            value = product.tradingArea
        )

        ProductInfoRow(
            label = "추천 체형",
            value = product.recommendedType.orEmpty()
        )

        ProductInfoRow(
            label = "판매 상태",
            value = product.postStatus
        )

        ProductInfoRow(
            label = "조회수",
            value = product.viewCount.toString()
        )

        ProductInfoRow(
            label = "찜",
            value = product.wishlistCount.toString()
        )
    }
}

@Composable
private fun SellerSection(
    sellerId: Long,
    onOpenSellerProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "판매자 정보",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOpenSellerProfile()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "판매자 ID: $sellerId",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444)
            )

            Text(
                text = "보기",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202C59)
            )
        }
    }
}

@Composable
private fun ProductDescriptionSection(
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "상품 설명",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF444444)
        )
    }
}

@Composable
private fun ProductReportSection(
    onReportProduct: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onReportProduct()
                },
            text = "이 상품 신고하기",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFB3261E),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )
    }
}