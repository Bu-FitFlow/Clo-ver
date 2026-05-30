package com.fitflow.clover.presentation.product

import androidx.lifecycle.ViewModel
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary
import com.fitflow.clover.presentation.trade.TradeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductViewModel : ViewModel() {

    companion object {
        private const val MAX_IMAGE_COUNT = 5
    }

    // 등록/수정 결과가 목록에 바로 반영되도록 기준 데이터는 ViewModel 내부에서 관리합니다.
    private var allProducts: List<ProductSummary> = dummyProducts

    // ─────────────────────────────────────────────────────────
    // 판매글 목록 상태
    // ─────────────────────────────────────────────────────────
    private val _listUiState = MutableStateFlow(
        ProductListUiState(products = dummyProducts)
    )
    val listUiState: StateFlow<ProductListUiState> = _listUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 판매관리 상태
    // ─────────────────────────────────────────────────────────
    private val _tradeUiState = MutableStateFlow(
        TradeUiState(
            isSellingTabSelected = true,
            sellingProducts = dummySellingProducts,
            soldProducts = dummySoldProducts
        )
    )
    val tradeUiState: StateFlow<TradeUiState> = _tradeUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 상품 등록/수정 상태
    // ─────────────────────────────────────────────────────────
    private val _editUiState = MutableStateFlow(ProductEditUiState())
    val editUiState: StateFlow<ProductEditUiState> = _editUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 판매글 목록 이벤트
    // ─────────────────────────────────────────────────────────

    fun onMainCategorySelect(category: ProductMainCategory) {
        val subList = if (category == ProductMainCategory.ALL) {
            emptyList()
        } else {
            ProductSubCategory.getByMainCategory(category)
        }

        _listUiState.update {
            it.copy(
                selectedMainCategory = category,
                isMainCategoryExpanded = false,
                selectedSubCategory = null,
                isSubCategoryExpanded = false,
                subCategoryList = subList,
                products = filterProducts(
                    main = category,
                    sub = null,
                    latestOrder = it.isLatestOrder
                ),
                errorMessage = null
            )
        }
    }

    fun onSubCategorySelect(subCategory: ProductSubCategory) {
        _listUiState.update {
            it.copy(
                selectedSubCategory = subCategory,
                isSubCategoryExpanded = false,
                products = filterProducts(
                    main = it.selectedMainCategory,
                    sub = subCategory,
                    latestOrder = it.isLatestOrder
                ),
                errorMessage = null
            )
        }
    }

    fun onMainCategoryExpandChange(isExpanded: Boolean) {
        _listUiState.update {
            it.copy(isMainCategoryExpanded = isExpanded)
        }
    }

    fun onSubCategoryExpandChange(isExpanded: Boolean) {
        _listUiState.update {
            it.copy(isSubCategoryExpanded = isExpanded)
        }
    }

    fun onFilterClick() {
        _listUiState.update { current ->
            val nextLatestOrder = !current.isLatestOrder
            current.copy(
                isLatestOrder = nextLatestOrder,
                products = filterProducts(
                    main = current.selectedMainCategory,
                    sub = current.selectedSubCategory,
                    latestOrder = nextLatestOrder
                )
            )
        }
    }

    private fun refreshProductList() {
        _listUiState.update { current ->
            current.copy(
                products = filterProducts(
                    main = current.selectedMainCategory,
                    sub = current.selectedSubCategory,
                    latestOrder = current.isLatestOrder
                )
            )
        }
    }

    private fun filterProducts(
        main: ProductMainCategory,
        sub: ProductSubCategory?,
        latestOrder: Boolean
    ): List<ProductSummary> {
        val filtered = allProducts.filter { product ->
            val mainMatch = main == ProductMainCategory.ALL || product.mainCategory == main
            val subMatch = sub == null || product.subCategory == sub
            mainMatch && subMatch
        }

        return if (latestOrder) {
            filtered.sortedByDescending { it.productId }
        } else {
            filtered.sortedBy { it.productId }
        }
    }

    // ─────────────────────────────────────────────────────────
    // 판매관리 이벤트
    // ─────────────────────────────────────────────────────────

    fun onSellingTabClick() {
        _tradeUiState.update {
            it.copy(isSellingTabSelected = true)
        }
    }

    fun onSoldTabClick() {
        _tradeUiState.update {
            it.copy(isSellingTabSelected = false)
        }
    }

    fun onStatusChange(productId: Long) {
        val selling = _tradeUiState.value.sellingProducts.toMutableList()
        val sold = _tradeUiState.value.soldProducts.toMutableList()

        val sellingProduct = selling.find { it.productId == productId }
        val soldProduct = sold.find { it.productId == productId }

        when {
            sellingProduct != null -> {
                selling.remove(sellingProduct)
                val completedProduct = sellingProduct.copy(isSold = true)
                sold.add(0, completedProduct)
                updateProductInList(completedProduct)
            }

            soldProduct != null -> {
                sold.remove(soldProduct)
                val sellingAgainProduct = soldProduct.copy(isSold = false)
                selling.add(0, sellingAgainProduct)
                updateProductInList(sellingAgainProduct)
            }
        }

        _tradeUiState.update {
            it.copy(
                sellingProducts = selling,
                soldProducts = sold
            )
        }
    }

    fun onProductDelete(productId: Long) {
        allProducts = allProducts.filter { it.productId != productId }

        _tradeUiState.update {
            it.copy(
                sellingProducts = it.sellingProducts.filter { product -> product.productId != productId },
                soldProducts = it.soldProducts.filter { product -> product.productId != productId }
            )
        }

        refreshProductList()
    }

    private fun updateProductInList(product: ProductSummary) {
        allProducts = allProducts.map {
            if (it.productId == product.productId) product else it
        }
        refreshProductList()
    }

    // ─────────────────────────────────────────────────────────
    // 상품 등록/수정 화면 준비
    // ─────────────────────────────────────────────────────────

    fun prepareRegister() {
        _editUiState.value = ProductEditUiState(
            isEditMode = false,
            productId = null,
            imageUris = emptyList(),
            isSubmitSuccess = false,
            errorMessage = null
        )
    }

    fun prepareEdit(productId: Long) {
        val product = allProducts.find { it.productId == productId }

        if (product == null) {
            _editUiState.value = ProductEditUiState(
                isEditMode = true,
                productId = productId,
                errorMessage = "수정할 상품을 찾을 수 없습니다."
            )
            return
        }

        val subList = ProductSubCategory.getByMainCategory(product.mainCategory)

        _editUiState.value = ProductEditUiState(
            isLoading = false,
            isEditMode = true,
            productId = product.productId,
            title = product.title,
            price = product.price.toString(),
            selectedMainCategory = product.mainCategory,
            selectedSubCategory = product.subCategory,
            subCategoryList = subList,
            imageUris = listOfNotNull(product.thumbnailImageUrl).take(MAX_IMAGE_COUNT),
            isSubmitting = false,
            isSubmitSuccess = false,
            errorMessage = null
        )
    }

    // ─────────────────────────────────────────────────────────
    // 상품 등록/수정 입력 이벤트
    // ─────────────────────────────────────────────────────────

    fun onEditTitleChange(title: String) {
        _editUiState.update {
            it.copy(title = title, errorMessage = null, isSubmitSuccess = false)
        }
    }

    fun onEditPriceChange(price: String) {
        val onlyDigits = price.filter { it.isDigit() }
        _editUiState.update {
            it.copy(price = onlyDigits, errorMessage = null, isSubmitSuccess = false)
        }
    }

    fun onEditDescriptionChange(description: String) {
        _editUiState.update {
            it.copy(description = description, errorMessage = null, isSubmitSuccess = false)
        }
    }

    fun onEditTradeLocationChange(location: String) {
        _editUiState.update {
            it.copy(tradeLocation = location, errorMessage = null, isSubmitSuccess = false)
        }
    }

    fun onEditSizeChange(size: String) {
        _editUiState.update {
            it.copy(size = size, errorMessage = null, isSubmitSuccess = false)
        }
    }

    fun onEditFitChange(fit: String) {
        _editUiState.update {
            it.copy(fit = fit, errorMessage = null, isSubmitSuccess = false)
        }
    }

    fun onEditMainCategorySelect(category: ProductMainCategory) {
        val subList = if (category == ProductMainCategory.ALL) {
            emptyList()
        } else {
            ProductSubCategory.getByMainCategory(category)
        }

        _editUiState.update {
            it.copy(
                selectedMainCategory = category,
                isMainCategoryExpanded = false,
                selectedSubCategory = null,
                isSubCategoryExpanded = false,
                subCategoryList = subList,
                errorMessage = null,
                isSubmitSuccess = false
            )
        }
    }

    fun onEditMainCategoryExpandChange(isExpanded: Boolean) {
        _editUiState.update {
            it.copy(isMainCategoryExpanded = isExpanded)
        }
    }

    fun onEditSubCategorySelect(subCategory: ProductSubCategory) {
        _editUiState.update {
            it.copy(
                selectedSubCategory = subCategory,
                isSubCategoryExpanded = false,
                errorMessage = null,
                isSubmitSuccess = false
            )
        }
    }

    fun onEditSubCategoryExpandChange(isExpanded: Boolean) {
        _editUiState.update {
            it.copy(isSubCategoryExpanded = isExpanded)
        }
    }

    fun onEditImageUrisChange(imageUris: List<String>) {
        _editUiState.update {
            it.copy(
                imageUris = imageUris.distinct().take(MAX_IMAGE_COUNT),
                errorMessage = null,
                isSubmitSuccess = false
            )
        }
    }

    fun onEditImageRemove(imageUri: String) {
        _editUiState.update {
            it.copy(
                imageUris = it.imageUris.filter { uri -> uri != imageUri },
                errorMessage = null,
                isSubmitSuccess = false
            )
        }
    }

    // 기존 ProductEditScreen 또는 NavHost가 예전 함수명을 쓰고 있어도 깨지지 않도록 별칭을 둡니다.
    fun onTitleChange(title: String) = onEditTitleChange(title)

    fun onPriceChange(price: String) = onEditPriceChange(price)

    fun onDescriptionChange(description: String) = onEditDescriptionChange(description)

    fun onTradeLocationChange(location: String) = onEditTradeLocationChange(location)

    fun onSizeChange(size: String) = onEditSizeChange(size)

    fun onFitChange(fit: String) = onEditFitChange(fit)

    fun onImageUrisChange(imageUris: List<String>) = onEditImageUrisChange(imageUris)

    // ─────────────────────────────────────────────────────────
    // 상품 등록/수정 완료
    // ─────────────────────────────────────────────────────────

    fun submitProduct(onSuccess: () -> Unit = {}) {
        val state = _editUiState.value
        val title = state.title.trim()
        val priceText = state.price.trim()
        val price = priceText.toIntOrNull()
        val mainCategory = state.selectedMainCategory
        val subCategory = state.selectedSubCategory

        when {
            title.isBlank() -> {
                showEditError("상품명을 입력해 주세요.")
                return
            }

            priceText.isBlank() -> {
                showEditError("가격을 입력해 주세요.")
                return
            }

            price == null || price <= 0 -> {
                showEditError("가격은 1원 이상 숫자로 입력해 주세요.")
                return
            }

            mainCategory == null || mainCategory == ProductMainCategory.ALL -> {
                showEditError("대분류를 선택해 주세요.")
                return
            }

            subCategory == null -> {
                showEditError("세부 카테고리를 선택해 주세요.")
                return
            }
        }

        _editUiState.update {
            it.copy(
                isSubmitting = true,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }

        if (state.isEditMode) {
            updateExistingProduct(
                state = state,
                title = title,
                price = price,
                mainCategory = mainCategory,
                subCategory = subCategory,
                onSuccess = onSuccess
            )
        } else {
            registerNewProduct(
                state = state,
                title = title,
                price = price,
                mainCategory = mainCategory,
                subCategory = subCategory,
                onSuccess = onSuccess
            )
        }
    }

    fun onSubmitClick() {
        submitProduct()
    }

    private fun registerNewProduct(
        state: ProductEditUiState,
        title: String,
        price: Int,
        mainCategory: ProductMainCategory,
        subCategory: ProductSubCategory,
        onSuccess: () -> Unit
    ) {
        val newProductId = generateNextProductId()
        val newProduct = ProductSummary(
            productId = newProductId,
            title = title,
            price = price,
            thumbnailImageUrl = state.imageUris.firstOrNull(),
            mainCategory = mainCategory,
            subCategory = subCategory,
            likeCount = 0,
            createdAt = "방금 전",
            isSold = false
        )

        allProducts = listOf(newProduct) + allProducts

        _tradeUiState.update {
            it.copy(
                sellingProducts = listOf(newProduct) + it.sellingProducts
            )
        }

        _editUiState.update {
            it.copy(
                isSubmitting = false,
                isSubmitSuccess = true,
                productId = newProductId,
                errorMessage = null
            )
        }

        refreshProductList()
        onSuccess()
    }

    private fun updateExistingProduct(
        state: ProductEditUiState,
        title: String,
        price: Int,
        mainCategory: ProductMainCategory,
        subCategory: ProductSubCategory,
        onSuccess: () -> Unit
    ) {
        val productId = state.productId

        if (productId == null) {
            _editUiState.update {
                it.copy(
                    isSubmitting = false,
                    isSubmitSuccess = false,
                    errorMessage = "수정할 상품 정보가 없습니다."
                )
            }
            return
        }

        val originProduct = allProducts.find { it.productId == productId }

        if (originProduct == null) {
            _editUiState.update {
                it.copy(
                    isSubmitting = false,
                    isSubmitSuccess = false,
                    errorMessage = "수정할 상품을 찾을 수 없습니다."
                )
            }
            return
        }

        val editedProduct = originProduct.copy(
            title = title,
            price = price,
            thumbnailImageUrl = state.imageUris.firstOrNull(),
            mainCategory = mainCategory,
            subCategory = subCategory
        )

        allProducts = allProducts.map {
            if (it.productId == productId) editedProduct else it
        }

        _tradeUiState.update {
            it.copy(
                sellingProducts = it.sellingProducts.map { product ->
                    if (product.productId == productId) editedProduct.copy(isSold = false) else product
                },
                soldProducts = it.soldProducts.map { product ->
                    if (product.productId == productId) editedProduct.copy(isSold = true) else product
                }
            )
        }

        _editUiState.update {
            it.copy(
                isSubmitting = false,
                isSubmitSuccess = true,
                errorMessage = null
            )
        }

        refreshProductList()
        onSuccess()
    }

    private fun showEditError(message: String) {
        _editUiState.update {
            it.copy(
                isSubmitting = false,
                isSubmitSuccess = false,
                errorMessage = message
            )
        }
    }

    private fun generateNextProductId(): Long {
        return ((allProducts + _tradeUiState.value.sellingProducts + _tradeUiState.value.soldProducts)
            .maxOfOrNull { it.productId } ?: 0L) + 1L
    }
}

// ─────────────────────────────────────────────────────────
// 더미 데이터
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
        isSold = false
    ),
    ProductSummary(
        productId = 5L,
        title = "데님 슬랙스",
        price = 25000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.PANTS,
        subCategory = ProductSubCategory.SLACKS,
        likeCount = 8,
        createdAt = "20분전",
        isSold = false
    ),
    ProductSummary(
        productId = 6L,
        title = "겨울 패딩",
        price = 55000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.OUTER,
        subCategory = ProductSubCategory.PADDING,
        likeCount = 20,
        createdAt = "30분전",
        isSold = false
    )
)

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
        title = "진청 반바지",
        price = 15000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.PANTS,
        subCategory = ProductSubCategory.SHORT_PANTS,
        likeCount = 5,
        createdAt = "15분전",
        isSold = true
    ),
    ProductSummary(
        productId = 5L,
        title = "데님 슬랙스",
        price = 25000,
        thumbnailImageUrl = null,
        mainCategory = ProductMainCategory.PANTS,
        subCategory = ProductSubCategory.SLACKS,
        likeCount = 3,
        createdAt = "20분전",
        isSold = true
    )
)
