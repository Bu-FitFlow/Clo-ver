package com.fitflow.clover.presentation.product

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitflow.clover.R
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory

private val CloverGreen = Color(0xFF99DE81)
private const val MaxProductImageCount = 5

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
    onImageUrisChange: (List<String>) -> Unit = {},
    onImagePickClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    val canAddImages = uiState.imageUris.size < MaxProductImageCount
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // 갤러리 런처
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = MaxProductImageCount),
        onResult = { selectedUris ->
            if (selectedUris.isNotEmpty()) {
                val selectedImageUris = selectedUris.map { it.toString() }
                val mergedUris = (uiState.imageUris + selectedImageUris)
                    .distinct()
                    .take(MaxProductImageCount)
                onImageUrisChange(mergedUris)
            }
        }
    )

    // 카메라 런처 - 임시 파일 없이 갤러리 앱의 카메라 기능 활용
    var pendingCameraUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                pendingCameraUri?.let { uri ->
                    val mergedUris = (uiState.imageUris + uri.toString())
                        .distinct()
                        .take(MaxProductImageCount)
                    onImageUrisChange(mergedUris)
                }
            }
            pendingCameraUri = null
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                val values = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "product_${System.currentTimeMillis()}.jpg")
                    put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val uri = context.contentResolver.insert(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
                if (uri != null) {
                    pendingCameraUri = uri
                    cameraLauncher.launch(uri)
                }
            }
        }
    )

    fun launchCamera() {
        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    // 이미지 소스 선택 다이얼로그 상태
    var showImageSourceDialog by remember { mutableStateOf(false) }

    if (showImageSourceDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("이미지 추가") },
            text = { Text("이미지를 어떻게 추가할까요?") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showImageSourceDialog = false
                    launchCamera()
                }) { Text("카메라로 찍기") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showImageSourceDialog = false
                    onImagePickClick()
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) { Text("갤러리에서 선택") }
            }
        )
    }

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
                        .background(Color.White)
                        .navigationBarsPadding()
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

                ProductImagePickerSection(
                    imageUris = uiState.imageUris,
                    canAddImages = canAddImages,
                    onAddImageClick = { showImageSourceDialog = true },
                    onImageRemove = { removeUri ->
                        onImageUrisChange(uiState.imageUris.filterNot { it == removeUri })
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = productTextFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductMainCategoryDropdown(
                        modifier = Modifier.weight(1f),
                        uiState = uiState,
                        onMainCategorySelect = onMainCategorySelect,
                        onMainCategoryExpandChange = onMainCategoryExpandChange
                    )

                    ProductSubCategoryDropdown(
                        modifier = Modifier.weight(1f),
                        uiState = uiState,
                        onSubCategorySelect = onSubCategorySelect,
                        onSubCategoryExpandChange = onSubCategoryExpandChange
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                    colors = productTextFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text("₩ 가격 입력", fontSize = 14.sp, color = Color.LightGray)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = productTextFieldColors(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = productTextFieldColors(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = productTextFieldColors(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = productTextFieldColors(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 이미지 첨부 영역: 최대 5장 선택, 미리보기, 삭제
// ─────────────────────────────────────────────────────────
@Composable
private fun ProductImagePickerSection(
    imageUris: List<String>,
    canAddImages: Boolean,
    onAddImageClick: () -> Unit,
    onImageRemove: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .clickable(
                    enabled = canAddImages,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onAddImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUris.isNotEmpty()) {
                AsyncImage(
                    model = imageUris.first(),
                    contentDescription = "대표 상품 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.18f))
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "이미지 추가",
                    tint = if (canAddImages) Color.Gray else Color.LightGray,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (canAddImages) "상품 이미지 추가" else "이미지는 최대 5장까지 가능",
                    fontSize = 13.sp,
                    color = if (canAddImages) Color.Gray else Color.LightGray,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "${imageUris.size}/$MaxProductImageCount",
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        if (imageUris.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(imageUris) { index, uri ->
                    ProductImageThumbnail(
                        uri = uri,
                        index = index,
                        onRemoveClick = { onImageRemove(uri) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductImageThumbnail(
    uri: String,
    index: Int,
    onRemoveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "상품 이미지 ${index + 1}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (index == 0) {
            Text(
                text = "대표",
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(
                        color = CloverGreen.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(topEnd = 6.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            )
        }

        Surface(
            onClick = onRemoveClick,
            color = Color.Black.copy(alpha = 0.65f),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(22.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "이미지 삭제",
                tint = Color.White,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 카테고리 드롭다운
// ─────────────────────────────────────────────────────────
@Composable
private fun ProductMainCategoryDropdown(
    modifier: Modifier = Modifier,
    uiState: ProductEditUiState,
    onMainCategorySelect: (ProductMainCategory) -> Unit,
    onMainCategoryExpandChange: (Boolean) -> Unit
) {
    Box(modifier = modifier) {
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
                    text = uiState.selectedMainCategory?.displayName ?: "카테고리(목록)",
                    fontSize = 13.sp,
                    color = if (uiState.selectedMainCategory != null) Color.Black else Color.Gray
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
}

@Composable
private fun ProductSubCategoryDropdown(
    modifier: Modifier = Modifier,
    uiState: ProductEditUiState,
    onSubCategorySelect: (ProductSubCategory) -> Unit,
    onSubCategoryExpandChange: (Boolean) -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            onClick = {
                if (uiState.selectedMainCategory != null) {
                    onSubCategoryExpandChange(!uiState.isSubCategoryExpanded)
                }
            },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                1.dp,
                if (uiState.selectedMainCategory != null) Color.LightGray else Color(0xFFDDDDDD)
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
                    text = uiState.selectedSubCategory?.displayName ?: "카테고리(상세)",
                    fontSize = 13.sp,
                    color = if (uiState.selectedSubCategory != null) Color.Black else Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (uiState.selectedMainCategory != null) Color.Black else Color.LightGray,
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

// ─────────────────────────────────────────────────────────
// 상단바: CommunityCloverTopBar와 동일한 구조
// ─────────────────────────────────────────────────────────
@Composable
fun ProductEditTopBar(
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

@Composable
private fun productTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Color.LightGray,
    focusedBorderColor = CloverGreen,
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White
)

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
        onSubCategoryExpandChange = { uiState = uiState.copy(isSubCategoryExpanded = it) },
        onImageUrisChange = { imageUris -> uiState = uiState.copy(imageUris = imageUris) }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductEditPreview() {
    var uiState by remember {
        mutableStateOf(
            ProductEditUiState(
                isEditMode = true,
                title = "나이키 후드",
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
        onSubCategoryExpandChange = { uiState = uiState.copy(isSubCategoryExpanded = it) },
        onImageUrisChange = { imageUris -> uiState = uiState.copy(imageUris = imageUris) }
    )
}