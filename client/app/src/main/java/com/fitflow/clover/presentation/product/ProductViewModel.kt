package com.fitflow.clover.presentation.product

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.fitflow.clover.data.remote.api.ProductApi
import com.fitflow.clover.data.repository.ProductRepositoryImpl
import com.fitflow.clover.di.NetworkModule
import com.fitflow.clover.domain.usecase.ProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productUseCase: ProductUseCase = ProductUseCase(
        productRepository = ProductRepositoryImpl(
            productApi = NetworkModule.createApi<ProductApi>()
        )
    )
) {
    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private val _listUiState = mutableStateOf(ProductListUiState())
    val listUiState: State<ProductListUiState> = _listUiState

    private val _detailUiState = mutableStateOf(ProductDetailUiState())
    val detailUiState: State<ProductDetailUiState> = _detailUiState

    private val _wishlistUiState = mutableStateOf(WishlistUiState())
    val wishlistUiState: State<WishlistUiState> = _wishlistUiState

    fun loadRecentProducts(
        size: Int = 9
    ) {
        if (_listUiState.value.isLoading) return

        viewModelScope.launch {
            _listUiState.value = ProductListUiState(
                isLoading = true,
                products = emptyList(),
                errorMessage = null
            )

            runCatching {
                productUseCase.getRecentProducts(
                    size = size
                )
            }.onSuccess { products ->
                _listUiState.value = ProductListUiState(
                    isLoading = false,
                    products = products,
                    errorMessage = null
                )
            }.onFailure { throwable ->
                _listUiState.value = ProductListUiState(
                    isLoading = false,
                    products = emptyList(),
                    errorMessage = throwable.message
                )
            }
        }
    }

    fun loadBodyRecommendedProducts(
        recommendedType: String,
        size: Int = 9
    ) {
        if (_listUiState.value.isLoading) return

        viewModelScope.launch {
            _listUiState.value = ProductListUiState(
                isLoading = true,
                products = emptyList(),
                errorMessage = null
            )

            runCatching {
                productUseCase.getBodyRecommendedProducts(
                    recommendedType = recommendedType,
                    size = size
                )
            }.onSuccess { products ->
                _listUiState.value = ProductListUiState(
                    isLoading = false,
                    products = products,
                    errorMessage = null
                )
            }.onFailure { throwable ->
                _listUiState.value = ProductListUiState(
                    isLoading = false,
                    products = emptyList(),
                    errorMessage = throwable.message
                )
            }
        }
    }

    fun loadProductDetail(
        productId: Long
    ) {
        if (_detailUiState.value.isLoading) return

        viewModelScope.launch {
            _detailUiState.value = ProductDetailUiState(
                isLoading = true,
                product = null,
                errorMessage = null
            )

            runCatching {
                productUseCase.getProductDetail(
                    productId = productId
                )
            }.onSuccess { product ->
                _detailUiState.value = ProductDetailUiState(
                    isLoading = false,
                    product = product,
                    errorMessage = null
                )
            }.onFailure { throwable ->
                _detailUiState.value = ProductDetailUiState(
                    isLoading = false,
                    product = null,
                    errorMessage = throwable.message
                )
            }
        }
    }

    fun toggleWishlist() {
        val currentProduct = _detailUiState.value.product ?: return

        if (_wishlistUiState.value.isProcessing) return

        viewModelScope.launch {
            _wishlistUiState.value = WishlistUiState(
                isProcessing = true,
                errorMessage = null
            )

            runCatching {
                if (currentProduct.isWishlisted) {
                    productUseCase.removeWishlist(
                        productId = currentProduct.productId
                    )
                } else {
                    productUseCase.addWishlist(
                        productId = currentProduct.productId
                    )
                }
            }.onSuccess { isWishlisted ->
                _detailUiState.value = _detailUiState.value.copy(
                    product = currentProduct.copy(
                        isWishlisted = isWishlisted
                    )
                )

                _wishlistUiState.value = WishlistUiState(
                    isProcessing = false,
                    errorMessage = null
                )
            }.onFailure { throwable ->
                _wishlistUiState.value = WishlistUiState(
                    isProcessing = false,
                    errorMessage = throwable.message
                )
            }
        }
    }

    fun clearListError() {
        _listUiState.value = _listUiState.value.copy(
            errorMessage = null
        )
    }

    fun clearDetailError() {
        _detailUiState.value = _detailUiState.value.copy(
            errorMessage = null
        )
    }

    fun clearWishlistError() {
        _wishlistUiState.value = _wishlistUiState.value.copy(
            errorMessage = null
        )
    }
}