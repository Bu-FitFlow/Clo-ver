package com.fitflow.clover.presentation.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary

private val CloverGreen = Color(0xFF99DE81)

// ─────────────────────────────────────────────────────────
// 1. 상품 카드
//    목록 화면과 판매관리 화면에서 공통으로 사용
// ─────────────────────────────────────────────────────────
@Composable
fun ProductCard(
    product: ProductSummary,
    onProductClick: (Long) -> Unit,
    onMenuClick: (Long) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 상품 썸네일 이미지
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = product.thumbnailImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // 거래완료 오버레이
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatPrice(product.price),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 작성 시간 + 좋아요 수
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = product.createdAt,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${product.likeCount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 더보기 버튼 (⋯)
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "더보기",
                tint = Color.LightGray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onMenuClick(product.productId) }
            )
        }

        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

// ─────────────────────────────────────────────────────────
// 2. 대분류 드롭다운 (전체/상의/바지/아우터/원피스·스커트)
// ─────────────────────────────────────────────────────────
@Composable
fun MainCategoryDropdown(
    selectedCategory: ProductMainCategory,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onCategorySelect: (ProductMainCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // 드롭다운 버튼
        Surface(
            onClick = { onExpandChange(!isExpanded) },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.DarkGray),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = selectedCategory.displayName,
                    fontSize = 13.sp,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Black
                )
            }
        }

        // 드롭다운 목록
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandChange(false) },
            containerColor = Color.White
        ) {
            ProductMainCategory.entries.forEach { category ->
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

// ─────────────────────────────────────────────────────────
// 3. 세부 드롭다운 (대분류 선택 시 자동 연동)
//    대분류가 ALL이면 비활성화
// ─────────────────────────────────────────────────────────
@Composable
fun SubCategoryDropdown(
    selectedSubCategory: ProductSubCategory?,
    subCategoryList: List<ProductSubCategory>,
    isExpanded: Boolean,
    isEnabled: Boolean,                      // 대분류 ALL이면 false
    onExpandChange: (Boolean) -> Unit,
    onSubCategorySelect: (ProductSubCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Surface(
            onClick = { if (isEnabled) onExpandChange(!isExpanded) },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                1.dp,
                if (isEnabled) Color.DarkGray else Color.LightGray
            ),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = selectedSubCategory?.displayName ?: "스타일",
                    fontSize = 13.sp,
                    color = if (isEnabled) Color.Black else Color.LightGray
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isEnabled) Color.Black else Color.LightGray
                )
            }
        }

        // 드롭다운 목록
        DropdownMenu(
            expanded = isExpanded,
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

// ─────────────────────────────────────────────────────────
// 4. 판매중 / 거래완료 탭
//    판매관리 화면 상단에서 사용
// ─────────────────────────────────────────────────────────
@Composable
fun TradeTabRow(
    isSellingTabSelected: Boolean,
    onSellingTabClick: () -> Unit,
    onSoldTabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 판매 중 탭
        Surface(
            onClick = onSellingTabClick,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                1.dp,
                if (!isSellingTabSelected) CloverGreen else Color.LightGray
            ),
            color = if (!isSellingTabSelected) CloverGreen else Color.White,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "판매 중",
                fontSize = 14.sp,
                fontWeight = if (!isSellingTabSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 거래 완료 탭
        Surface(
            onClick = onSoldTabClick,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                1.dp,
                if (isSellingTabSelected) CloverGreen else Color.LightGray
            ),
            color = if (isSellingTabSelected) CloverGreen else Color.White,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "거래 완료",
                fontSize = 14.sp,
                fontWeight = if (isSellingTabSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 가격 포맷 함수
// 38900 → "38,900원"
// ─────────────────────────────────────────────────────────
fun formatPrice(price: Int): String {
    return "%,d원".format(price)
}