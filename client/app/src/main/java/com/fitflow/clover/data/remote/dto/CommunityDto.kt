package com.fitflow.clover.data.remote.dto

import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.modal.CommunityReply
import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// ─────────────────────────────────────────────────────────
// 1. 게시글 목록 응답
// ─────────────────────────────────────────────────────────

data class CommunityPostSummaryResponse(
    @SerializedName("communityId")
    val communityId: Long? = null,

    @SerializedName("writerId")
    val writerId: Long? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("viewCount")
    val viewCount: Int? = null,

    @SerializedName("commentCount")
    val commentCount: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
)

// ─────────────────────────────────────────────────────────
// 2. 게시글 상세 응답
// ─────────────────────────────────────────────────────────

data class CommunityPostDetailResponse(
    @SerializedName("communityId")
    val communityId: Long? = null,

    @SerializedName("writerId")
    val writerId: Long? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("ootdInfo")
    val ootdInfo: String? = null,

    @SerializedName("imageUrls")
    val imageUrls: List<String>? = null,

    @SerializedName("writerNickname")
    val writerNickname: String? = null,

    @SerializedName("writerProfileImg")
    val writerProfileImg: String? = null,

    @SerializedName("viewCount")
    val viewCount: Int? = null,

    @SerializedName("commentCount")
    val commentCount: Int? = null,

    @SerializedName("wishlistCount")
    val wishlistCount: Int? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("comments")
    val comments: List<CommunityCommentResponse>? = null,

    // 백엔드 연결 후 추가될 필드 (현재 API에 없으면 null 처리)
    @SerializedName("isMyPost")
    val isMyPost: Boolean? = null
)

// ─────────────────────────────────────────────────────────
// 3. 댓글 응답
// ─────────────────────────────────────────────────────────

data class CommunityCommentResponse(
    @SerializedName("commentId")
    val commentId: Long? = null,

    @SerializedName("writerNickname")
    val writerNickname: String? = null,

    @SerializedName("writerProfileImg")
    val writerProfileImg: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("isMyComment")
    val isMyComment: Boolean? = null,

    @SerializedName("replies")
    val replies: List<CommunityReplyResponse>? = null
)

// ─────────────────────────────────────────────────────────
// 4. 대댓글 응답
// ─────────────────────────────────────────────────────────

data class CommunityReplyResponse(
    @SerializedName("replyId")
    val replyId: Long? = null,

    @SerializedName("writerNickname")
    val writerNickname: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("isMyReply")
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

    @SerializedName("imageUrl")
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

    @SerializedName("imageUrl")
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
        postId = communityId ?: 0L,
        authorNickname = "",                 // 목록 API에 writerNickname 없음 → 상세에서 채워짐
        category = CommunityCategory.FREE,   // 목록 API에 category 없음 → 기본값
        title = title.orEmpty(),
        contentPreview = "",                 // 목록 API에 content 없음
        authorProfileImageUrl = null,
        likeCount = 0,
        commentCount = commentCount ?: 0,
        createdAt = createdAt.toRelativeTime(),
        thumbnailImageUrl = null
    )
}

fun CommunityPostDetailResponse.toDomain(): CommunityPost {
    val contentBlocks = buildList<CommunityContentBlock> {
        if (!content.isNullOrBlank()) {
            add(CommunityContentBlock.TextBlock(content))
        }
        if (!ootdInfo.isNullOrBlank()) {
            add(CommunityContentBlock.TextBlock(ootdInfo))
        }
        imageUrls?.forEach { imageUrl ->
            if (imageUrl.isNotBlank()) {
                add(CommunityContentBlock.ImageBlock(imageUrl = imageUrl, description = null))
            }
        }
    }

    return CommunityPost(
        postId = communityId ?: 0L,
        authorNickname = writerNickname.orEmpty(),
        category = CommunityCategory.FREE,
        title = title.orEmpty(),
        contentBlocks = contentBlocks,
        authorProfileImageUrl = writerProfileImg,
        likeCount = wishlistCount ?: 0,
        isLiked = false,                     // API에 없음 → 추후 추가
        commentCount = commentCount ?: 0,
        comments = comments?.map { it.toDomain() }.orEmpty(),
        createdAt = createdAt.toRelativeTime(),
        isMyPost = isMyPost ?: false
    )
}

fun CommunityCommentResponse.toDomain(): CommunityComment {
    return CommunityComment(
        commentId = commentId ?: 0L,
        authorNickname = writerNickname.orEmpty(),
        authorProfileImageUrl = writerProfileImg,
        content = content.orEmpty(),
        createdAt = createdAt.toRelativeTime(),
        isMyComment = isMyComment ?: false,
        replies = replies?.map { it.toDomain() }.orEmpty()
    )
}

fun CommunityReplyResponse.toDomain(): CommunityReply {
    return CommunityReply(
        replyId = replyId ?: 0L,
        authorNickname = writerNickname.orEmpty(),
        content = content.orEmpty(),
        createdAt = createdAt.toRelativeTime(),
        isMyReply = isMyReply ?: false
    )
}

// ─────────────────────────────────────────────────────────
// 10. ISO 8601 → "N분 전" 변환 유틸
// ─────────────────────────────────────────────────────────

private fun String?.toRelativeTime(): String {
    if (this.isNullOrBlank()) return ""
    return try {
        val dateTime = ZonedDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        val now = ZonedDateTime.now(dateTime.zone)
        val minutes = ChronoUnit.MINUTES.between(dateTime, now)
        when {
            minutes < 1    -> "방금 전"
            minutes < 60   -> "${minutes}분 전"
            minutes < 1440 -> "${minutes / 60}시간 전"
            minutes < 10080 -> "${minutes / 1440}일 전"
            else           -> "${minutes / 10080}주 전"
        }
    } catch (e: Exception) {
        this
    }
}