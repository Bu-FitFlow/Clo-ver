package com.fitflow.clover.presentation.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.fitflow.clover.data.remote.api.ProductApi
import com.fitflow.clover.data.repository.ProductRepositoryImpl
import com.fitflow.clover.di.NetworkModule
import com.fitflow.clover.domain.modal.ProductSummaryModel
import com.fitflow.clover.domain.usecase.ProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = false,
    val hasBodyDiagnosis: Boolean = false,
    val recentProducts: List<ProductSummaryModel> = emptyList(),
    val bodyRecommendProducts: List<ProductSummaryModel> = emptyList(),
    val errorMessage: String? = null
)

data class MainCommunityPostUiModel(
    val id: Long,
    val title: String,
    val nickname: String,
    val date: String,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int
)

class MainViewModel(
    private val productUseCase: ProductUseCase = ProductUseCase(
        productRepository = ProductRepositoryImpl(
            productApi = NetworkModule.createApi<ProductApi>()
        )
    )
) {
    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private val _uiState = mutableStateOf(MainUiState())
    val uiState: State<MainUiState> = _uiState

    val latestCommunityPosts: List<MainCommunityPostUiModel> = listOf(
        MainCommunityPostUiModel(
            id = 1L,
            title = "한정판 콜라보 티셔츠 솔직한 후기",
            nickname = "닉네임",
            date = "2026-04-24",
            viewCount = 120,
            commentCount = 4,
            likeCount = 20
        ),
        MainCommunityPostUiModel(
            id = 2L,
            title = "중고 거래할 때 확인해야 할 점",
            nickname = "클로버",
            date = "2026-04-25",
            viewCount = 98,
            commentCount = 3,
            likeCount = 15
        ),
        MainCommunityPostUiModel(
            id = 3L,
            title = "데님 자켓 코디 추천 받아요",
            nickname = "스타일러",
            date = "2026-04-26",
            viewCount = 210,
            commentCount = 8,
            likeCount = 31
        )
    )

    val popularCommunityPosts: List<MainCommunityPostUiModel> = listOf(
        MainCommunityPostUiModel(
            id = 4L,
            title = "겨울 쿨톤에게 잘 맞는 색상 정리",
            nickname = "톤잘알",
            date = "2026-04-24",
            viewCount = 340,
            commentCount = 12,
            likeCount = 45
        ),
        MainCommunityPostUiModel(
            id = 5L,
            title = "체형별 아우터 고르는 방법",
            nickname = "핏마스터",
            date = "2026-04-25",
            viewCount = 286,
            commentCount = 9,
            likeCount = 38
        ),
        MainCommunityPostUiModel(
            id = 6L,
            title = "중고 의류 판매 사진 잘 찍는 팁",
            nickname = "클로버가이드",
            date = "2026-04-26",
            viewCount = 254,
            commentCount = 7,
            likeCount = 34
        )
    )

    fun loadMainProducts(
        bodyType: String?,
        size: Int = 9
    ) {
        val requestSize = 9
        val recommendedType = normalizeBodyTypeOrNull(bodyType)
        val hasBodyDiagnosis = recommendedType != null

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                hasBodyDiagnosis = hasBodyDiagnosis,
                errorMessage = null
            )

            runCatching {
                val recentProducts = productUseCase.getRecentProducts(
                    size = requestSize
                )

                val bodyRecommendProducts = if (recommendedType == null) {
                    emptyList()
                } else {
                    productUseCase.getBodyRecommendedProducts(
                        recommendedType = recommendedType,
                        size = requestSize
                    )
                }

                recentProducts to bodyRecommendProducts
            }.onSuccess { result ->
                val recentProducts = limitMainProducts(
                    source = result.first,
                    size = requestSize
                )

                val bodyRecommendProducts = limitMainProducts(
                    source = result.second,
                    size = requestSize
                )

                _uiState.value = MainUiState(
                    isLoading = false,
                    hasBodyDiagnosis = hasBodyDiagnosis,
                    recentProducts = recentProducts,
                    bodyRecommendProducts = bodyRecommendProducts,
                    errorMessage = null
                )
            }.onFailure { throwable ->
                _uiState.value = MainUiState(
                    isLoading = false,
                    hasBodyDiagnosis = hasBodyDiagnosis,
                    recentProducts = emptyList(),
                    bodyRecommendProducts = emptyList(),
                    errorMessage = throwable.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    private fun normalizeBodyTypeOrNull(bodyType: String?): String? {
        return when (
            bodyType
                ?.trim()
                ?.uppercase()
                ?.replace("-", "_")
                ?.replace(" ", "_")
        ) {
            "TRIANGLE", "PEAR", "A_LINE" -> "TRIANGLE"
            "INVERTED_TRIANGLE", "INVERTED" -> "INVERTED_TRIANGLE"
            "OVAL", "APPLE", "ROUND" -> "OVAL"
            "HOURGLASS", "HOUR_GLASS", "HOUR_GLASS_SHAPE" -> "HOURGLASS"
            "RECTANGLE", "LEAN_COLUMN", "COLUMN", "STRAIGHT" -> "RECTANGLE"
            else -> null
        }
    }
}

private fun limitMainProducts(
    source: List<ProductSummaryModel>,
    size: Int
): List<ProductSummaryModel> {
    return source
        .filter { product ->
            product.postStatus.equals(
                other = "ACTIVE",
                ignoreCase = true
            )
        }
        .distinctBy { product ->
            product.productId
        }
        .take(size)
}