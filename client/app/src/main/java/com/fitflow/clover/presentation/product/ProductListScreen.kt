package com.fitflow.clover.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProductListScreen(
    recommendedType: String? = null,
    onBack: () -> Unit,
    onClickProduct: (Long) -> Unit
) {
    val productViewModel = remember {
        ProductViewModel()
    }

    val listUiState = productViewModel.listUiState.value

    LaunchedEffect(recommendedType) {
        if (recommendedType.isNullOrBlank()) {
            productViewModel.loadRecentProducts()
        } else {
            productViewModel.loadBodyRecommendedProducts(
                recommendedType = recommendedType
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ProductListTopBar(
            title = if (recommendedType.isNullOrBlank()) {
                "최근 등록 상품"
            } else {
                "체형 추천 상품"
            },
            onBack = onBack
        )

        when {
            listUiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            listUiState.errorMessage != null -> {
                ProductListErrorContent(
                    message = listUiState.errorMessage,
                    onRetry = {
                        if (recommendedType.isNullOrBlank()) {
                            productViewModel.loadRecentProducts()
                        } else {
                            productViewModel.loadBodyRecommendedProducts(
                                recommendedType = recommendedType
                            )
                        }
                    }
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(listUiState.products) { product ->
                        ProductSummaryCard(
                            product = product,
                            onClick = {
                                onClickProduct(product.productId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductListTopBar(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onBack
        ) {
            Text(text = "이전")
        }

        Spacer(modifier = Modifier.padding(start = 12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
private fun ProductListErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF777777)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry
        ) {
            Text(text = "다시 불러오기")
        }
    }
}