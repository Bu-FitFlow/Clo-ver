package com.fitflow.clover.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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

private val CloverGreen = Color(0xFF99DE81)

// ─────────────────────────────────────────────────────────
// 상품 등록/수정 화면
// ─────────────────────────────────────────────────────────
@Composable
fun ProductEditScreen(
    uiState: ProductEditUiState = ProductEditUiState(),
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onPriceChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onTradeLocationChange: (String) -> Unit = {},
    onSizeChange: (String) -> Unit = {},
    onFitChange: (String) -> Unit = {},
    onMainCategorySelect: (ProductMainCategory) -> Unit = {},
    onMainCategoryExpandChange: (Boolean) -> Unit = {},
    onSubCategorySelect: (ProductSubCategory) -> Unit = {},
    onSubCategoryExpandChange: (Boolean) -> Unit = {},
    onImagePickClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                ProductEditTopBar(onBackClick = onBackClick)
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = onSubmitClick,
                        enabled = !uiState.isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CloverGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = if (uiState.isEditMode) "수정" else "등록",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // ─── 이미지 업로드 영역 ───
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onImagePickClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "이미지 추가",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "${uiState.imageUris.size}/5",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 상품명 입력 ───
                Text(
                    text = "상품명 입력",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 카테고리 드롭다운 ───
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 대분류
                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            onClick = {
                                onMainCategoryExpandChange(!uiState.isMainCategoryExpanded)
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = uiState.selectedMainCategory?.displayName
                                        ?: "카테고리(목록)",
                                    fontSize = 13.sp,
                                    color = if (uiState.selectedMainCategory != null)
                                        Color.Black else Color.Gray
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = uiState.isMainCategoryExpanded,
                            onDismissRequest = { onMainCategoryExpandChange(false) },
                            containerColor = Color.White
                        ) {
                            ProductMainCategory.entries
                                .filter { it != ProductMainCategory.ALL }
                                .forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = category.displayName,
                                                fontSize = 14.sp,
                                                color = Color.Black
                                            )
                                        },
                                        onClick = {
                                            onMainCategorySelect(category)
                                            onMainCategoryExpandChange(false)
                                        }
                                    )
                                }
                        }
                    }

                    // 세부분류
                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            onClick = {
                                if (uiState.selectedMainCategory != null) {
                                    onSubCategoryExpandChange(!uiState.isSubCategoryExpanded)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                1.dp,
                                if (uiState.selectedMainCategory != null)
                                    Color.LightGray else Color(0xFFDDDDDD)
                            ),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = uiState.selectedSubCategory?.displayName
                                        ?: "카테고리(상세)",
                                    fontSize = 13.sp,
                                    color = if (uiState.selectedSubCategory != null)
                                        Color.Black else Color.Gray
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = if (uiState.selectedMainCategory != null)
                                        Color.Black else Color.LightGray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = uiState.isSubCategoryExpanded,
                            onDismissRequest = { onSubCategoryExpandChange(false) },
                            containerColor = Color.White
                        ) {
                            uiState.subCategoryList.forEach { subCategory ->
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
                                        onSubCategoryExpandChange(false)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 상품 정보 ───
                Text(
                    text = "상품 정보",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 판매 가격 ───
                Text(
                    text = "판매 가격",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = uiState.price,
                    onValueChange = onPriceChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    placeholder = {
                        Text("₩ 가격 입력", fontSize = 14.sp, color = Color.LightGray)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 거래 지역 ───
                Text(
                    text = "거래 지역",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = uiState.tradeLocation,
                    onValueChange = onTradeLocationChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 사이즈 ───
                Text(
                    text = "사이즈",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = uiState.size,
                    onValueChange = onSizeChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ─── 핏 선택 사항 ───
                Text(
                    text = "핏 선택 사항",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = uiState.fit,
                    onValueChange = onFitChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 상단바
// ─────────────────────────────────────────────────────────
@Composable
fun ProductEditTopBar(
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F8E0))
            .statusBarsPadding()
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
// Preview
// ─────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductRegisterPreview() {
    var uiState by remember { mutableStateOf(ProductEditUiState(isEditMode = false)) }
    ProductEditScreen(
        uiState = uiState,
        onTitleChange = { uiState = uiState.copy(title = it) },
        onPriceChange = { uiState = uiState.copy(price = it) },
        onDescriptionChange = { uiState = uiState.copy(description = it) },
        onTradeLocationChange = { uiState = uiState.copy(tradeLocation = it) },
        onSizeChange = { uiState = uiState.copy(size = it) },
        onFitChange = { uiState = uiState.copy(fit = it) },
        onMainCategorySelect = { category ->
            uiState = uiState.copy(
                selectedMainCategory = category,
                isMainCategoryExpanded = false,
                subCategoryList = ProductSubCategory.getByMainCategory(category),
                selectedSubCategory = null
            )
        },
        onMainCategoryExpandChange = { uiState = uiState.copy(isMainCategoryExpanded = it) },
        onSubCategorySelect = { subCategory ->
            uiState = uiState.copy(
                selectedSubCategory = subCategory,
                isSubCategoryExpanded = false
            )
        },
        onSubCategoryExpandChange = { uiState = uiState.copy(isSubCategoryExpanded = it) }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductEditPreview() {
    var uiState by remember {
        mutableStateOf(
            ProductEditUiState(
                isEditMode = true,
                title = "나이키 후드 (거의 새것)",
                price = "39800",
                description = "깨끗하게 입었습니다",
                tradeLocation = "서울 강남구",
                size = "M",
                fit = "오버핏",
                selectedMainCategory = ProductMainCategory.TOP,
                subCategoryList = ProductSubCategory.getByMainCategory(ProductMainCategory.TOP),
                selectedSubCategory = ProductSubCategory.HOODIE
            )
        )
    }
    ProductEditScreen(
        uiState = uiState,
        onTitleChange = { uiState = uiState.copy(title = it) },
        onPriceChange = { uiState = uiState.copy(price = it) },
        onDescriptionChange = { uiState = uiState.copy(description = it) },
        onTradeLocationChange = { uiState = uiState.copy(tradeLocation = it) },
        onSizeChange = { uiState = uiState.copy(size = it) },
        onFitChange = { uiState = uiState.copy(fit = it) },
        onMainCategorySelect = { category ->
            uiState = uiState.copy(
                selectedMainCategory = category,
                isMainCategoryExpanded = false,
                subCategoryList = ProductSubCategory.getByMainCategory(category),
                selectedSubCategory = null
            )
        },
        onMainCategoryExpandChange = { uiState = uiState.copy(isMainCategoryExpanded = it) },
        onSubCategorySelect = { subCategory ->
            uiState = uiState.copy(
                selectedSubCategory = subCategory,
                isSubCategoryExpanded = false
            )
        },
        onSubCategoryExpandChange = { uiState = uiState.copy(isSubCategoryExpanded = it) }
    )
}