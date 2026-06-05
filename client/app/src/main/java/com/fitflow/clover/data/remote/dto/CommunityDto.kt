package com.fitflow.clover.data.remote.dto

import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.modal.CommunityReply
import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────
// 1. 게시글 목록 응답
// ─────────────────────────────────────────────────────────

data class CommunityPostSummaryResponse(
    @SerializedName("post_id")
    val postId: Long? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("content_preview")
    val contentPreview: String? = null,

    @SerializedName("author_nickname")
    val authorNickname: String? = null,

    @SerializedName("author_profile_image_url")
    val authorProfileImageUrl: String? = null,

    @SerializedName("like_count")
    val likeCount: Int? = null,

    @SerializedName("comment_count")
    val commentCount: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("thumbnail_image_url")
    val thumbnailImageUrl: String? = null
)

// ─────────────────────────────────────────────────────────
// 2. 게시글 상세 응답
// ─────────────────────────────────────────────────────────

data class CommunityPostDetailResponse(
    @SerializedName("post_id")
    val postId: Long? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("author_nickname")
    val authorNickname: String? = null,

    @SerializedName("author_profile_image_url")
    val authorProfileImageUrl: String? = null,

    @SerializedName("like_count")
    val likeCount: Int? = null,

    @SerializedName("is_liked")
    val isLiked: Boolean? = null,

    @SerializedName("comment_count")
    val commentCount: Int? = null,

    @SerializedName("comments")
    val comments: List<CommunityCommentResponse>? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("is_my_post")
    val isMyPost: Boolean? = null
)

// ─────────────────────────────────────────────────────────
// 3. 댓글 응답
// ─────────────────────────────────────────────────────────

data class CommunityCommentResponse(
    @SerializedName("comment_id")
    val commentId: Long? = null,

    @SerializedName("author_nickname")
    val authorNickname: String? = null,

    @SerializedName("author_profile_image_url")
    val authorProfileImageUrl: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("is_my_comment")
    val isMyComment: Boolean? = null,

    @SerializedName("replies")
    val replies: List<CommunityReplyResponse>? = null
)

// ─────────────────────────────────────────────────────────
// 4. 대댓글 응답
// ─────────────────────────────────────────────────────────

data class CommunityReplyResponse(
    @SerializedName("reply_id")
    val replyId: Long? = null,

    @SerializedName("author_nickname")
    val authorNickname: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("is_my_reply")
    val isMyReply: Boolean? = null
)

// ─────────────────────────────────────────────────────────
// 5. 게시글 작성 요청
// ─────────────────────────────────────────────────────────

data class CreateCommunityPostRequest(
    @SerializedName("category")
    val category: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("image_url")
    val imageUrl: String? = null
)

// ─────────────────────────────────────────────────────────
// 6. 게시글 수정 요청
// ─────────────────────────────────────────────────────────

data class UpdateCommunityPostRequest(
    @SerializedName("category")
    val category: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("image_url")
    val imageUrl: String? = null
)

// ─────────────────────────────────────────────────────────
// 7. 댓글 작성 요청
// ─────────────────────────────────────────────────────────

data class CreateCommentRequest(
    @SerializedName("content")
    val content: String
)

// ─────────────────────────────────────────────────────────
// 8. 대댓글 작성 요청
// ─────────────────────────────────────────────────────────

data class CreateReplyRequest(
    @SerializedName("content")
    val content: String
)

// ─────────────────────────────────────────────────────────
// 9. toDomain 변환 함수
// ─────────────────────────────────────────────────────────

fun CommunityPostSummaryResponse.toDomain(): CommunityPostSummary {
    return CommunityPostSummary(
        postId = postId ?: 0L,
        category = category.toCommunityCategory(),
        title = title.orEmpty(),
        contentPreview = contentPreview.orEmpty(),
        authorNickname = authorNickname.orEmpty(),
        authorProfileImageUrl = authorProfileImageUrl,
        likeCount = likeCount ?: 0,
        commentCount = commentCount ?: 0,
        createdAt = createdAt.orEmpty(),
        thumbnailImageUrl = thumbnailImageUrl
    )
}

fun CommunityPostDetailResponse.toDomain(): CommunityPost {
    val contentBlocks = buildList<CommunityContentBlock> {
        if (!content.isNullOrBlank()) {
            add(CommunityContentBlock.TextBlock(content))
        }
        if (!imageUrl.isNullOrBlank()) {
            add(CommunityContentBlock.ImageBlock(imageUrl = imageUrl, description = "첨부 이미지"))
        }
    }

    return CommunityPost(
        postId = postId ?: 0L,
        category = category.toCommunityCategory(),
        title = title.orEmpty(),
        contentBlocks = contentBlocks,
        authorNickname = authorNickname.orEmpty(),
        authorProfileImageUrl = authorProfileImageUrl,
        likeCount = likeCount ?: 0,
        isLiked = isLiked ?: false,
        commentCount = commentCount ?: 0,
        comments = comments?.map { it.toDomain() }.orEmpty(),
        createdAt = createdAt.orEmpty(),
        isMyPost = isMyPost ?: false
    )
}

fun CommunityCommentResponse.toDomain(): CommunityComment {
    return CommunityComment(
        commentId = commentId ?: 0L,
        authorNickname = authorNickname.orEmpty(),
        authorProfileImageUrl = authorProfileImageUrl,
        content = content.orEmpty(),
        createdAt = createdAt.orEmpty(),
        isMyComment = isMyComment ?: false,
        replies = replies?.map { it.toDomain() }.orEmpty()
    )
}

fun CommunityReplyResponse.toDomain(): CommunityReply {
    return CommunityReply(
        replyId = replyId ?: 0L,
        authorNickname = authorNickname.orEmpty(),
        content = content.orEmpty(),
        createdAt = createdAt.orEmpty(),
        isMyReply = isMyReply ?: false
    )
}

private fun String?.toCommunityCategory(): CommunityCategory {
    return when (this?.uppercase()) {
        "FREE" -> CommunityCategory.FREE
        "REVIEW" -> CommunityCategory.REVIEW
        "COORDINATION" -> CommunityCategory.COORDINATION
        else -> CommunityCategory.FREE
    }
}