package com.fitflow.clover.presentation.community

import android.net.Uri
import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary

// ─────────────────────────────────────────
// 1. 게시판 목록 화면 상태
// ─────────────────────────────────────────
data class CommunityListUiState(
    val isLoading: Boolean = false,
    val posts: List<CommunityPostSummary> = emptyList(),
    val selectedCategory: CommunityCategory = CommunityCategory.ALL,
    val searchQuery: String = "",
    val isWriteMenuExpanded: Boolean = false,
    val errorMessage: String? = null,
    // 목록에서 ... 버튼으로 열리는 게시글별 팝업 메뉴
    // null이면 닫힘, Long 값이면 해당 postId의 메뉴가 열림
    val openedPostMenuId: Long? = null,
    // 열린 메뉴가 내 글인지 여부 (내 글이면 수정/삭제, 아니면 신고/차단)
    val openedPostMenuIsMyPost: Boolean = false
)

// ─────────────────────────────────────────
// 2. 게시글 상세 화면 상태
// ─────────────────────────────────────────
data class CommunityDetailUiState(
    val isLoading: Boolean = false,
    val post: CommunityPost? = null,
    val commentInput: String = "",
    val isMenuExpanded: Boolean = false,
    val errorMessage: String? = null,
    // 대댓글: 현재 답글을 달 대상 댓글 ID (null이면 입력창 숨김)
    val replyTargetCommentId: Long? = null,
    val replyInput: String = "",
    // 댓글 수정: 현재 수정 중인 댓글 ID (null이면 수정 모드 아님)
    val editingCommentId: Long? = null,
    val editingCommentInput: String = "",
    // 대댓글 수정: 현재 수정 중인 대댓글 ID
    val editingReplyId: Long? = null,
    val editingReplyInput: String = ""
)

// ─────────────────────────────────────────
// 3. 게시글 작성 화면 상태
// ─────────────────────────────────────────
data class CommunityWriteUiState(
    val title: String = "",
    val selectedCategory: CommunityCategory = CommunityCategory.FREE,
    val isCategoryDropdownExpanded: Boolean = false,
    val selectedImageUri: Uri? = null,
    val contentBlocks: List<CommunityContentBlock> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val errorMessage: String? = null
)

// ─────────────────────────────────────────
// 4. 게시글 수정 화면 상태
// ─────────────────────────────────────────
data class CommunityEditUiState(
    val isLoading: Boolean = false,
    val postId: Long? = null,
    val title: String = "",
    val selectedCategory: CommunityCategory = CommunityCategory.FREE,
    val isCategoryDropdownExpanded: Boolean = false,
    val selectedImageUri: Uri? = null,
    val contentBlocks: List<CommunityContentBlock> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val errorMessage: String? = null
)