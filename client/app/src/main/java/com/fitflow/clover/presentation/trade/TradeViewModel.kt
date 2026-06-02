package com.fitflow.clover.presentation.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitflow.clover.domain.modal.ProductMainCategory
import com.fitflow.clover.domain.modal.ProductSubCategory
import com.fitflow.clover.domain.modal.ProductSummary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TradeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TradeUiState())
    val uiState: StateFlow<TradeUiState> = _uiState.asStateFlow()

    init {
        loadTradeProducts()
    }

    // ─────────────────────────────────────────────────────────
    // 초기 판매관리 목록 로드
    // ─────────────────────────────────────────────────────────
    fun loadTradeProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                delay(300)
                Pair(dummySellingProducts, dummySoldProducts)
            }.onSuccess { (sellingProducts, soldProducts) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sellingProducts = sellingProducts,
                        soldProducts = soldProducts,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "판매관리 목록을 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    // 탭 전환
    // ─────────────────────────────────────────────────────────
    fun onSellingTabClick() {
        _uiState.update {
            it.copy(isSellingTabSelected = true)
        }
    }

    fun onSoldTabClick() {
        _uiState.update {
            it.copy(isSellingTabSelected = false)
        }
    }

    // ─────────────────────────────────────────────────────────
    // 상품 상태 변경
    // 판매 중 탭에서는 거래완료로 이동,
    // 거래 완료 탭에서는 판매중으로 이동합니다.
    // ─────────────────────────────────────────────────────────
    fun onStatusChangeClick(productId: Long) {
        val current = _uiState.value

        if (current.isSellingTabSelected) {
            val targetProduct = current.sellingProducts.find { it.productId == productId } ?: return
            val changedProduct = targetProduct.copy(isSold = true)

            _uiState.update {
                it.copy(
                    sellingProducts = it.sellingProducts.filterNot { product ->
                        product.productId == productId
                    },
                    soldProducts = listOf(changedProduct) + it.soldProducts,
                    errorMessage = null
                )
            }
        } else {
            val targetProduct = current.soldProducts.find { it.productId == productId } ?: return
            val changedProduct = targetProduct.copy(isSold = false)

            _uiState.update {
                it.copy(
                    soldProducts = it.soldProducts.filterNot { product ->
                        product.productId == productId
                    },
                    sellingProducts = listOf(changedProduct) + it.sellingProducts,
                    errorMessage = null
                )
            }
        }
    }

    // 기존 화면 연결부에서 다른 함수명을 사용할 때를 대비한 별칭입니다.
    fun toggleProductStatus(productId: Long) = onStatusChangeClick(productId)

    fun changeTradeStatus(productId: Long) = onStatusChangeClick(productId)

    // ─────────────────────────────────────────────────────────
    // 상품 삭제
    // ─────────────────────────────────────────────────────────
    fun onDeleteClick(productId: Long) {
        _uiState.update {
            it.copy(
                sellingProducts = it.sellingProducts.filterNot { product ->
                    product.productId == productId
                },
                soldProducts = it.soldProducts.filterNot { product ->
                    product.productId == productId
                },
                errorMessage = null
            )
        }
    }

    fun deleteProduct(productId: Long) = onDeleteClick(productId)

    // ─────────────────────────────────────────────────────────
    // 상품 수정 진입 준비
    // 실제 수정 화면 이동은 NavHost에서 처리하고,
    // 이 함수는 필요할 때 수정 대상 상품을 찾는 용도로 사용할 수 있습니다.
    // ─────────────────────────────────────────────────────────
    fun findProduct(productId: Long): ProductSummary? {
        val current = _uiState.value
        return current.sellingProducts.find { it.productId == productId }
            ?: current.soldProducts.find { it.productId == productId }
    }

    fun onEditClick(productId: Long): ProductSummary? {
        return findProduct(productId)
    }

    // ─────────────────────────────────────────────────────────
    // 상품 등록 후 판매관리에도 반영하고 싶을 때 사용하는 함수
    // ProductViewModel에서 상품 등록 성공 후 이 함수를 호출하면 됩니다.
    // ─────────────────────────────────────────────────────────
    fun addSellingProduct(product: ProductSummary) {
        val sellingProduct = product.copy(isSold = false)

        _uiState.update {
            it.copy(
                sellingProducts = listOf(sellingProduct) + it.sellingProducts.filterNot { currentProduct ->
                    currentProduct.productId == product.productId
                },
                soldProducts = it.soldProducts.filterNot { currentProduct ->
                    currentProduct.productId == product.productId
                },
                errorMessage = null
            )
        }
    }

    // ─────────────────────────────────────────────────────────
    // 상품 수정 완료 후 판매관리 목록 갱신
    // 수정 화면에서 저장이 끝난 뒤 호출하면 됩니다.
    // ─────────────────────────────────────────────────────────
    fun updateProduct(product: ProductSummary) {
        _uiState.update {
            it.copy(
                sellingProducts = it.sellingProducts.map { currentProduct ->
                    if (currentProduct.productId == product.productId) {
                        product.copy(isSold = false)
                    } else {
                        currentProduct
                    }
                },
                soldProducts = it.soldProducts.map { currentProduct ->
                    if (currentProduct.productId == product.productId) {
                        product.copy(isSold = true)
                    } else {
                        currentProduct
                    }
                },
                errorMessage = null
            )
        }
    }

    // ─────────────────────────────────────────────────────────
    // 오류 메시지 초기화
    // ─────────────────────────────────────────────────────────
    fun clearErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    companion object {
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
    }
}
