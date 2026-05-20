package com.fitflow.clover.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.R
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary

private val CloverGreen = Color(0xFF99DE81)

// ─────────────────────────────────────────────────────────
// 판매글 목록 화면
// ─────────────────────────────────────────────────────────
@Composable
fun ProductListScreen(
    uiState: ProductListUiState = ProductListUiState(),
    onProductClick: (Long) -> Unit = {},
    onMenuClick: (Long) -> Unit = {},
    onMainCategorySelect: (ProductMainCategory) -> Unit = {},
    onMainCategoryExpandChange: (Boolean) -> Unit = {},
    onSubCategorySelect: (ProductSubCategory) -> Unit = {},
    onSubCategoryExpandChange: (Boolean) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    // 로고 상단바
                    ProductTopBar(onBackClick = onBackClick)

                    // 드롭다운 + 검색 + 필터 버튼 행
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 대분류 드롭다운 (전체)
                        MainCategoryDropdown(
                            selectedCategory = uiState.selectedMainCategory,
                            isExpanded = uiState.isMainCategoryExpanded,
                            onExpandChange = onMainCategoryExpandChange,
                            onCategorySelect = onMainCategorySelect
                        )

                        // 세부 드롭다운 (스타일) - 항상 활성화
                        SubCategoryDropdown(
                            selectedSubCategory = uiState.selectedSubCategory,
                            subCategoryList = uiState.subCategoryList,
                            isExpanded = uiState.isSubCategoryExpanded,
                            isEnabled = true,
                            onExpandChange = onSubCategoryExpandChange,
                            onSubCategorySelect = onSubCategorySelect
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // 검색 버튼
                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색",
                                tint = Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // 필터 버튼 (최신순) - 토글 없이 단순 클릭
                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                                contentDescription = "최신순 필터",
                                tint = Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onWriteClick,
                    containerColor = CloverGreen,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "글쓰기",
                        tint = Color.White
                    )
                }
            }
        ) { paddingValues ->
            when {
                // 로딩 중
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CloverGreen)
                    }
                }
                // 에러
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                // 상품 없음
                uiState.products.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "등록된 상품이 없어요",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                // 2열 그리드 피드
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.products) { product ->
                            ProductGridCard(
                                product = product,
                                onProductClick = onProductClick
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 2열 그리드용 상품 카드
// ─────────────────────────────────────────────────────────
@Composable
fun ProductGridCard(
    product: ProductSummary,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onProductClick(product.productId) }
    ) {
        // 상품 이미지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEEEEEE))
        ) {
            if (product.thumbnailImageUrl != null) {
                coil.compose.AsyncImage(
                    model = product.thumbnailImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            if (product.isSold) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "거래완료",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 상품 제목
        Text(
            text = product.title,
            fontSize = 13.sp,
            color = Color.Black,
            maxLines = 2,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        // 가격
        Text(
            text = formatPrice(product.price),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(2.dp))

        // 작성 시간 + 좋아요 (좋아요 오른쪽 정렬)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.createdAt,
                fontSize = 11.sp,
                color = Color.Gray
            )

            // 좋아요 (빨간 하트 + 숫자)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(11.dp)
                )
                Text(
                    text = "${product.likeCount}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 공통 - 로고 상단바
// ─────────────────────────────────────────────────────────
@Composable
fun ProductTopBar(
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F8E0))
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back_icon),
            contentDescription = "뒤로가기",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onBackClick() }
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Clo-ver 로고",
                modifier = Modifier
                    .width(58.dp)
                    .height(45.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.size(48.dp))
    }
}

// ─────────────────────────────────────────────────────────
// Preview 더미 데이터
// ─────────────────────────────────────────────────────────
private val dummyProducts = listOf(
    ProductSummary(
        productId = 1L,
        title = "나이키 후드 (거의 새것)",
        price = 38900,
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
        likeCount = 15,
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
        likeCount = 15,
        createdAt = "10분전",
        isSold = false
    ),
    ProductSummary(
        productId = 4L,
        title = "진청 반바지 (미착용)",
        price = 15000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.PANTS,
        subCategory = ProductSubCategory.SHORT_PANTS,
        likeCount = 15,
        createdAt = "15분전",
        isSold = true
    ),
    ProductSummary(
        productId = 5L,
        title = "폴로 니트",
        price = 40000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.TOP,
        subCategory = ProductSubCategory.KNIT_SWEATER,
        likeCount = 15,
        createdAt = "10분전",
        isSold = false
    ),
    ProductSummary(
        productId = 6L,
        title = "진청 반바지 (미착용)",
        price = 15000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.PANTS,
        subCategory = ProductSubCategory.SHORT_PANTS,
        likeCount = 15,
        createdAt = "15분전",
        isSold = false
    )
)

// ─────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductListPreview() {
    var uiState by remember {
        mutableStateOf(ProductListUiState(products = dummyProducts))
    }
    ProductListScreen(
        uiState = uiState,
        onMainCategorySelect = { category ->
            uiState = uiState.copy(
                selectedMainCategory = category,
                isMainCategoryExpanded = false,
                subCategoryList = if (category == ProductMainCategory.ALL) {
                    emptyList()
                } else {
                    ProductSubCategory.getByMainCategory(category)
                },
                selectedSubCategory = null,
                isSubCategoryExpanded = false
            )
        },
        onMainCategoryExpandChange = { isExpanded ->
            uiState = uiState.copy(isMainCategoryExpanded = isExpanded)
        },
        onSubCategorySelect = { subCategory ->
            uiState = uiState.copy(
                selectedSubCategory = subCategory,
                isSubCategoryExpanded = false
            )
        },
        onSubCategoryExpandChange = { isExpanded ->
            uiState = uiState.copy(isSubCategoryExpanded = isExpanded)
        },
        onFilterClick = {
            uiState = uiState.copy(isLatestOrder = !uiState.isLatestOrder)
        }
    )
}