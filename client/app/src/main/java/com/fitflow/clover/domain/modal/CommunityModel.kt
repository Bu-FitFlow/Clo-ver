package com.fitflow.clover.domain.modal

/**
 * 커뮤니티 카테고리
 * 서버에서 문자열로 오더라도 앱 내부에선 enum으로 관리
 */
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

/**
 * 게시글 목록 아이템 (목록 화면용 - 상세보다 가벼운 데이터)
 * CommunityListScreen의 PostItem에 사용
 */
data class CommunityPostSummary(
    val postId: Long,
    val category: CommunityCategory,
    val title: String,
    val contentPreview: String,      // 본문 앞부분만 잘라서 미리보기
    val authorNickname: String,
    val authorProfileImageUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: String,           // "1시간 전" 같은 포맷은 ViewModel에서 변환
    val thumbnailImageUrl: String?   // 게시글 대표 이미지 (없을 수도 있음)
)

/**
 * 게시글 본문 블록 타입
 * 상세 화면 보면 텍스트-이미지-설명텍스트가 섞여서 나옴
 * 순서가 중요하기 때문에 리스트로 관리
 */
sealed class CommunityContentBlock {
    data class TextBlock(val text: String) : CommunityContentBlock()
    data class ImageBlock(
        val imageUrl: String,
        val description: String?     // 이미지 아래 설명 (없으면 "-설명 없을 때-" 표시)
    ) : CommunityContentBlock()
}

/**
 * 게시글 상세 (상세 화면용 - 전체 데이터)
 * CommunityDetailScreen에 사용
 */
data class CommunityPost(
    val postId: Long,
    val category: CommunityCategory,
    val title: String,
    val contentBlocks: List<CommunityContentBlock>,  // 텍스트+이미지 혼합 블록
    val authorNickname: String,
    val authorProfileImageUrl: String?,
    val likeCount: Int,
    val isLiked: Boolean,                // 현재 로그인 유저가 좋아요 눌렀는지
    val commentCount: Int,
    val comments: List<CommunityComment>,
    val createdAt: String,
    val isMyPost: Boolean                // 수정/삭제 버튼 노출 여부 판단용
)

/**
 * 댓글
 * 상세 화면 댓글 섹션에 사용
 * 대댓글은 replies 리스트로 포함
 */
data class CommunityComment(
    val commentId: Long,
    val authorNickname: String,
    val authorProfileImageUrl: String?,
    val content: String,
    val createdAt: String,
    val isMyComment: Boolean,            // 내 댓글이면 삭제 버튼 노출
    val replies: List<CommunityReply>    // 대댓글 목록
)

/**
 * 대댓글
 * 댓글 아래에 들여쓰기로 표시
 */
data class CommunityReply(
    val replyId: Long,
    val authorNickname: String,
    val content: String,
    val createdAt: String,
    val isMyReply: Boolean
)

/**
 * 게시글 작성/수정 요청용 모델
 * CommunityWriteScreen → ViewModel → Repository로 전달할 때 사용
 */
data class CommunityPostRequest(
    val category: CommunityCategory,
    val title: String,
    val contentBlocks: List<CommunityContentBlock>,
    val imageUris: List<String>          // 로컬에서 선택한 이미지 URI 목록
)