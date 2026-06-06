package com.fitflow.clover.mypage

data class MyPageUiState(
    val name: String = "",
    val email: String = "",
    val nickname: String = "",
    val loginId: String = "",
    val displayLoginId: String = loginId.ifBlank { email },

    val profileImageModel: String? = null,
    val heightLabel: String = "선택 안 됨",
    val weightLabel: String = "선택 안 됨",
    val personalColorLabel: String = "선택 안 됨",
    val bodyTypeLabel: String = "선택 안 됨",

    val cloverProgress: Float = 0.0f,
    val myProducts: List<MyPageProductItem> = emptyList(),
    val myPosts: List<MyPagePostItem> = emptyList()
) {
    val displayName: String
        get() = nickname.ifBlank {
            name.ifBlank {
                "CLOVER"
            }
        }
}

data class MyPageProductItem(
    val id: Int = 0,
    val thumbnailImageUrl: String = "",
    val name: String = "",
    val price: String = "",
    val postStatus: String = ""
)

data class MyPagePostItem(
    val id: Int = 0,
    val category: String = "",
    val title: String = "",
    val content: String = "",
    val commentCount: Int = 0,
    val createdAt: String = "",
    val boardType: String = category
)

typealias MyPagePostUiState = MyPagePostItem