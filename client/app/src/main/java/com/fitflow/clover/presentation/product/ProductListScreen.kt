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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProductListClick: () -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onMyPageClick: () -> Unit = {},
    onSellClick: () -> Unit = {},
    onCommunityWriteClick: () -> Unit = {}
) {
    var isFabMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProductTopBar(onBackClick = onBackClick)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MainCategoryDropdown(
                            selectedCategory = uiState.selectedMainCategory,
                            isExpanded = uiState.isMainCategoryExpanded,
                            onExpandChange = onMainCategoryExpandChange,
                            onCategorySelect = onMainCategorySelect
                        )

                        SubCategoryDropdown(
                            selectedSubCategory = uiState.selectedSubCategory,
                            subCategoryList = uiState.subCategoryList,
                            isExpanded = uiState.isSubCategoryExpanded,
                            isEnabled = true,
                            onExpandChange = onSubCategoryExpandChange,
                            onSubCategorySelect = onSubCategorySelect
                        )

                        Spacer(modifier = Modifier.weight(1f))

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

                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = "최신순 필터",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = CloverGreen)
                        }
                    }
                    uiState.errorMessage != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    uiState.products.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "등록된 상품이 없어요",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize(),
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

                if (isFabMenuExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { isFabMenuExpanded = false }
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.width(140.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                ProductMenuItem("알림") {
                                    isFabMenuExpanded = false
                                    onNotificationClick()
                                }
                                ProductMenuItem("채팅방") {
                                    isFabMenuExpanded = false
                                    onChatClick()
                                }
                                ProductMenuItem("판매글") {
                                    isFabMenuExpanded = false
                                    onProductListClick()
                                }
                                ProductMenuItem("커뮤니티") {
                                    isFabMenuExpanded = false
                                    onCommunityClick()
                                }
                                ProductMenuItem("마이페이지") {
                                    isFabMenuExpanded = false
                                    onMyPageClick()
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.width(120.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                ProductMenuItem("판매") {
                                    isFabMenuExpanded = false
                                    onSellClick()
                                }
                                ProductMenuItem("글쓰기") {
                                    isFabMenuExpanded = false
                                    onCommunityWriteClick()
                                }
                            }
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.listbar),
                    contentDescription = "메뉴 열기",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                        .size(56.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { isFabMenuExpanded = !isFabMenuExpanded }
                )
            }
        }
    }
}

@Composable
private fun ProductMenuItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}


// ─────────────────────────────────────────────────────────
// 카테고리 드롭다운
// ─────────────────────────────────────────────────────────
@Composable
private fun MainCategoryDropdown(
    selectedCategory: ProductMainCategory,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onCategorySelect: (ProductMainCategory) -> Unit
) {
    Box {
        CategoryChip(
            text = selectedCategory.displayName,
            isExpanded = isExpanded,
            onClick = { onExpandChange(!isExpanded) }
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandChange(false) },
            containerColor = Color.White
        ) {
            ProductMainCategory.values().forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.displayName,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onCategorySelect(category)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun SubCategoryDropdown(
    selectedSubCategory: ProductSubCategory?,
    subCategoryList: List<ProductSubCategory>,
    isExpanded: Boolean,
    isEnabled: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onSubCategorySelect: (ProductSubCategory) -> Unit
) {
    val enabled = isEnabled && subCategoryList.isNotEmpty()

    Box {
        CategoryChip(
            text = selectedSubCategory?.displayName ?: "스타일",
            isExpanded = isExpanded,
            enabled = enabled,
            onClick = {
                if (enabled) {
                    onExpandChange(!isExpanded)
                }
            }
        )

        DropdownMenu(
            expanded = isExpanded && enabled,
            onDismissRequest = { onExpandChange(false) },
            containerColor = Color.White
        ) {
            subCategoryList.forEach { subCategory ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = subCategory.displayName,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onSubCategorySelect(subCategory)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isExpanded: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (enabled) Color(0xFFF7F7F7) else Color(0xFFEDEDED),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier
            .height(36.dp)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = if (enabled) Color.Black else Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = if (enabled) Color.Black else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
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
        // 이미지
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
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

        // 텍스트 영역: 제목(2줄 고정) + 가격 + 날짜/하트
        Column(modifier = Modifier.fillMaxWidth()) {
            // 제목: 항상 2줄 높이 고정
            Text(
                text = product.title,
                fontSize = 13.sp,
                color = Color.Black,
                maxLines = 2,
                minLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = formatPrice(product.price),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(2.dp))

            // 날짜 + 하트: 항상 같은 줄
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.createdAt,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
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
}

// ─────────────────────────────────────────────────────────
// 공통 - 로고 상단바
// ─────────────────────────────────────────────────────────
@Composable
fun ProductTopBar(
    onBackClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .background(Color.White)
        )
        Surface(
            color = Color(0xFFE8F8E0),
            modifier = Modifier
                .fillMaxWidth()
                .height(57.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                            .width(80.dp)
                            .height(62.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// Preview 더미 데이터
// ─────────────────────────────────────────────────────────
private val dummyProducts = listOf(
    ProductSummary(
        productId = 1L,
        title = "나이키 후드",
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
        title = "진청 반바지",
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
        title = "진청 반바지",
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
    var showEdit by remember { mutableStateOf(false) }
    var uiState by remember {
        mutableStateOf(ProductListUiState(products = dummyProducts))
    }

    if (showEdit) {
        ProductEditScreen(onBackClick = { showEdit = false })
        return@ProductListPreview
    }

    ProductListScreen(
        uiState = uiState,
        onCommunityWriteClick = { showEdit = true },
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