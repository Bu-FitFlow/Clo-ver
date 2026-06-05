package com.fitflow.clover.domain.modal

enum class CommunityCategory(val displayName: String) {
    ALL("전체"),
    FREE("자유"),
    REVIEW("리뷰"),
    COORDINATION("코디");

    companion object {
        fun fromString(value: String): CommunityCategory {
            return entries.find { it.displayName == value } ?: ALL
        }
    }
}

data class CommunityPostSummary(
    val postId: Long,
    val authorNickname: String,          // 차단 시 nickname으로 API 호출
    val category: CommunityCategory,
    val title: String,
    val contentPreview: String,
    val authorProfileImageUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: String,
    val thumbnailImageUrl: String?
)

sealed class CommunityContentBlock {
    data class TextBlock(val text: String) : CommunityContentBlock()
    data class ImageBlock(
        val imageUrl: String,
        val description: String?
    ) : CommunityContentBlock()
}

data class CommunityPost(
    val postId: Long,
    val authorNickname: String,          // 차단 시 nickname으로 API 호출
    val category: CommunityCategory,
    val title: String,
    val contentBlocks: List<CommunityContentBlock>,
    val authorProfileImageUrl: String?,
    val likeCount: Int,
    val isLiked: Boolean,
    val commentCount: Int,
    val comments: List<CommunityComment>,
    val createdAt: String,
    val isMyPost: Boolean
)

data class CommunityComment(
    val commentId: Long,
    val authorNickname: String,
    val authorProfileImageUrl: String?,
    val content: String,
    val createdAt: String,
    val isMyComment: Boolean,
    val replies: List<CommunityReply>
)

data class CommunityReply(
    val replyId: Long,
    val authorNickname: String,
    val content: String,
    val createdAt: String,
    val isMyReply: Boolean
)

data class CommunityPostRequest(
    val category: CommunityCategory,
    val title: String,
    val contentBlocks: List<CommunityContentBlock>,
    val imageUris: List<String>
)