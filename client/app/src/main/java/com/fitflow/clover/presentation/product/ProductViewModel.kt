package com.fitflow.clover.presentation.product

import androidx.lifecycle.ViewModel
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {

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

    // 대분류 선택 → 세부 카테고리 자동 연동
    fun onMainCategorySelect(category: ProductMainCategory) {
        val subList = if (category == ProductMainCategory.ALL) {
            emptyList()
        } else {
            ProductSubCategory.getByMainCategory(category)
        }

        _listUiState.value = _listUiState.value.copy(
            selectedMainCategory = category,
            isMainCategoryExpanded = false,
            subCategoryList = subList,
            selectedSubCategory = null,
            isSubCategoryExpanded = false,
            products = filterProducts(category, null)
        )
    }

    // 세부 카테고리 선택
    fun onSubCategorySelect(subCategory: ProductSubCategory) {
        _listUiState.value = _listUiState.value.copy(
            selectedSubCategory = subCategory,
            isSubCategoryExpanded = false,
            products = filterProducts(
                _listUiState.value.selectedMainCategory,
                subCategory
            )
        )
    }

    // 대분류 드롭다운 열림/닫힘
    fun onMainCategoryExpandChange(isExpanded: Boolean) {
        _listUiState.value = _listUiState.value.copy(
            isMainCategoryExpanded = isExpanded
        )
    }

    // 세부 드롭다운 열림/닫힘
    fun onSubCategoryExpandChange(isExpanded: Boolean) {
        _listUiState.value = _listUiState.value.copy(
            isSubCategoryExpanded = isExpanded
        )
    }

    // 필터 버튼 → 최신순 정렬
    fun onFilterClick() {
        _listUiState.value = _listUiState.value.copy(
            products = _listUiState.value.products.reversed(),
            isLatestOrder = !_listUiState.value.isLatestOrder
        )
    }

    // 카테고리 필터링 함수
    private fun filterProducts(
        main: ProductMainCategory,
        sub: ProductSubCategory?
    ): List<ProductSummary> {
        return dummyProducts.filter { product ->
            val mainMatch = main == ProductMainCategory.ALL ||
                    product.mainCategory == main
            val subMatch = sub == null || product.subCategory == sub
            mainMatch && subMatch
        }
    }

    // ─────────────────────────────────────────────────────────
    // 판매관리 이벤트
    // ─────────────────────────────────────────────────────────

    // 판매 중 탭 클릭
    fun onSellingTabClick() {
        _tradeUiState.value = _tradeUiState.value.copy(
            isSellingTabSelected = true
        )
    }

    // 거래 완료 탭 클릭
    fun onSoldTabClick() {
        _tradeUiState.value = _tradeUiState.value.copy(
            isSellingTabSelected = false
        )
    }

    // 판매중 ↔ 거래완료 상태 변경
    fun onStatusChange(productId: Long) {
        val selling = _tradeUiState.value.sellingProducts.toMutableList()
        val sold = _tradeUiState.value.soldProducts.toMutableList()

        val sellingProduct = selling.find { it.productId == productId }
        val soldProduct = sold.find { it.productId == productId }

        when {
            // 판매중 → 거래완료로 변경
            sellingProduct != null -> {
                selling.remove(sellingProduct)
                sold.add(sellingProduct.copy(isSold = true))
            }
            // 거래완료 → 판매중으로 변경
            soldProduct != null -> {
                sold.remove(soldProduct)
                selling.add(soldProduct.copy(isSold = false))
            }
        }

        _tradeUiState.value = _tradeUiState.value.copy(
            sellingProducts = selling,
            soldProducts = sold
        )
    }

    // 상품 삭제
    fun onProductDelete(productId: Long) {
        _tradeUiState.value = _tradeUiState.value.copy(
            sellingProducts = _tradeUiState.value.sellingProducts
                .filter { it.productId != productId },
            soldProducts = _tradeUiState.value.soldProducts
                .filter { it.productId != productId }
        )
    }

    // ─────────────────────────────────────────────────────────
    // 상품 등록/수정 이벤트
    // ─────────────────────────────────────────────────────────

    fun onTitleChange(title: String) {
        _editUiState.value = _editUiState.value.copy(title = title)
    }

    fun onPriceChange(price: String) {
        _editUiState.value = _editUiState.value.copy(price = price)
    }

    fun onDescriptionChange(description: String) {
        _editUiState.value = _editUiState.value.copy(description = description)
    }

    fun onTradeLocationChange(location: String) {
        _editUiState.value = _editUiState.value.copy(tradeLocation = location)
    }

    fun onSizeChange(size: String) {
        _editUiState.value = _editUiState.value.copy(size = size)
    }

    fun onFitChange(fit: String) {
        _editUiState.value = _editUiState.value.copy(fit = fit)
    }

    fun onEditMainCategorySelect(category: ProductMainCategory) {
        _editUiState.value = _editUiState.value.copy(
            selectedMainCategory = category,
            isMainCategoryExpanded = false,
            subCategoryList = ProductSubCategory.getByMainCategory(category),
            selectedSubCategory = null
        )
    }

    fun onEditMainCategoryExpandChange(isExpanded: Boolean) {
        _editUiState.value = _editUiState.value.copy(
            isMainCategoryExpanded = isExpanded
        )
    }

    fun onEditSubCategorySelect(subCategory: ProductSubCategory) {
        _editUiState.value = _editUiState.value.copy(
            selectedSubCategory = subCategory,
            isSubCategoryExpanded = false
        )
    }

    fun onEditSubCategoryExpandChange(isExpanded: Boolean) {
        _editUiState.value = _editUiState.value.copy(
            isSubCategoryExpanded = isExpanded
        )
    }

    // 등록/수정 완료
    fun onSubmitClick() {
        val state = _editUiState.value
        if (state.title.isEmpty() ||
            state.price.isEmpty() ||
            state.selectedMainCategory == null
        ) return

        _editUiState.value = _editUiState.value.copy(isSubmitSuccess = true)
    }
}

// ─────────────────────────────────────────────────────────
// 더미 데이터
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
        title = "겨울 패딩 판매해요",
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
        title = "나이키 후드 (거의 새것)",
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
        title = "진청 반바지 (미착용)",
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

