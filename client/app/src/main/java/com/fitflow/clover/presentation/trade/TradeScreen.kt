package com.fitflow.clover.presentation.trade

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitflow.clover.R
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary
import com.fitflow.clover.presentation.product.TradeUiState
import com.fitflow.clover.presentation.product.formatPrice

private val CloverGreen = Color(0xFF99DE81)

// ─────────────────────────────────────────────────────────
// 판매관리 화면
// ─────────────────────────────────────────────────────────
@Composable
fun TradeScreen(
    uiState: TradeUiState = TradeUiState(),
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSellingTabClick: () -> Unit = {},
    onSoldTabClick: () -> Unit = {},
    onStatusChangeClick: (Long) -> Unit = {},
    onEditClick: (Long) -> Unit = {},
    onDeleteClick: (Long) -> Unit = {}
) {
    // 메뉴 열린 상품 ID 상태 관리
    var expandedProductId by remember { mutableStateOf<Long?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TradeTopBar(
                    onBackClick = onBackClick,
                    onNotificationClick = onNotificationClick
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 판매 중 / 거래 완료 탭
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 판매 중 탭
                    Surface(
                        onClick = onSellingTabClick,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (uiState.isSellingTabSelected) CloverGreen else Color.LightGray
                        ),
                        color = if (uiState.isSellingTabSelected) CloverGreen else Color.White,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "판매 중",
                            fontSize = 14.sp,
                            fontWeight = if (uiState.isSellingTabSelected) FontWeight.Bold else FontWeight.Normal,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                    // 거래 완료 탭
                    Surface(
                        onClick = onSoldTabClick,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (!uiState.isSellingTabSelected) CloverGreen else Color.LightGray
                        ),
                        color = if (!uiState.isSellingTabSelected) CloverGreen else Color.White,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "거래 완료",
                            fontSize = 14.sp,
                            fontWeight = if (!uiState.isSellingTabSelected) FontWeight.Bold else FontWeight.Normal,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = CloverGreen)
                        }
                    }

                    else -> {
                        // 탭에 따라 목록 전환
                        val currentList = if (uiState.isSellingTabSelected) {
                            uiState.sellingProducts
                        } else {
                            uiState.soldProducts
                        }

                        if (currentList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.isSellingTabSelected) {
                                        "판매 중인 상품이 없어요"
                                    } else {
                                        "거래 완료된 상품이 없어요"
                                    },
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(currentList) { product ->
                                    TradeProductCard(
                                        product = product,
                                        isMenuExpanded = expandedProductId == product.productId,
                                        isSellingTab = uiState.isSellingTabSelected,
                                        onMenuClick = {
                                            expandedProductId =
                                                if (expandedProductId == product.productId) null
                                                else product.productId
                                        },
                                        onStatusChangeClick = {
                                            onStatusChangeClick(product.productId)
                                            expandedProductId = null
                                        },
                                        onEditClick = {
                                            onEditClick(product.productId)
                                            expandedProductId = null
                                        },
                                        onDeleteClick = {
                                            onDeleteClick(product.productId)
                                            expandedProductId = null
                                        },
                                        onDismiss = { expandedProductId = null }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 판매관리 상품 카드
// ─────────────────────────────────────────────────────────
@Composable
fun TradeProductCard(
    product: ProductSummary,
    isMenuExpanded: Boolean,
    isSellingTab: Boolean,
    onMenuClick: () -> Unit,
    onStatusChangeClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 상품 이미지
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                if (product.thumbnailImageUrl != null) {
                    AsyncImage(
                        model = product.thumbnailImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 상품 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatPrice(product.price),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // ⋯ 더보기 버튼 + 드롭다운 메뉴
            Box {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "더보기",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onMenuClick() }
                )

                // 드롭다운 메뉴
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = onDismiss,
                    containerColor = Color.White
                ) {
                    // 탭에 따라 첫 번째 메뉴 텍스트 변경
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (isSellingTab) "거래완료로 변경" else "판매중으로 변경",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        },
                        onClick = onStatusChangeClick
                    )
                    DropdownMenuItem(
                        text = { Text("수정", fontSize = 14.sp, color = Color.Black) },
                        onClick = onEditClick
                    )
                    DropdownMenuItem(
                        text = { Text("삭제", fontSize = 14.sp, color = Color.Red) },
                        onClick = onDeleteClick
                    )
                    DropdownMenuItem(
                        text = { Text("닫기", fontSize = 14.sp, color = Color.Gray) },
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 판매관리 상단바
// ─────────────────────────────────────────────────────────
@Composable
fun TradeTopBar(
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "뒤로가기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBackClick() }
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "판매관리",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "알림",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onNotificationClick() }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// Preview 더미 데이터
// ─────────────────────────────────────────────────────────
private val dummySellingProducts = listOf(
    ProductSummary(
        productId = 1L,
        title = "나이키 후드",
        price = 39800,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.TOP,
        subCategory = ProductSubCategory.HOODIE,
        likeCount = 15,
        createdAt = "1분전",
        isSold = false
    ),
    ProductSummary(
        productId = 2L,
        title = "꾸안꾸 티셔츠",
        price = 10000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.TOP,
        subCategory = ProductSubCategory.SHORT_SLEEVE,
        likeCount = 5,
        createdAt = "5분전",
        isSold = false
    ),
    ProductSummary(
        productId = 3L,
        title = "폴로 니트",
        price = 40000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.TOP,
        subCategory = ProductSubCategory.KNIT_SWEATER,
        likeCount = 8,
        createdAt = "10분전",
        isSold = false
    )
)

private val dummySoldProducts = listOf(
    ProductSummary(
        productId = 4L,
        title = "나이키 후드",
        price = 39800,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.TOP,
        subCategory = ProductSubCategory.HOODIE,
        likeCount = 15,
        createdAt = "1분전",
        isSold = true
    ),
    ProductSummary(
        productId = 5L,
        title = "꾸안꾸 티셔츠",
        price = 10000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.TOP,
        subCategory = ProductSubCategory.SHORT_SLEEVE,
        likeCount = 5,
        createdAt = "5분전",
        isSold = true
    )
)

// ─────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TradeSellingPreview() {
    var uiState by remember {
        mutableStateOf(
            TradeUiState(
                isSellingTabSelected = true,
                sellingProducts = dummySellingProducts,
                soldProducts = dummySoldProducts
            )
        )
    }
    TradeScreen(
        uiState = uiState,
        onSellingTabClick = {
            uiState = uiState.copy(isSellingTabSelected = true)
        },
        onSoldTabClick = {
            uiState = uiState.copy(isSellingTabSelected = false)
        }
    )
}
