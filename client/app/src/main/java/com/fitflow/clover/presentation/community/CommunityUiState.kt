package com.fitflow.clover.presentation.community

import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary

// ─────────────────────────────────────────
// 1. 게시판 목록 화면 상태
// ─────────────────────────────────────────
data class CommunityListUiState(
    val isLoading: Boolean = false,               // 로딩 스피너 표시 여부
    val posts: List<CommunityPostSummary> = emptyList(), // 게시글 목록
    val selectedCategory: CommunityCategory = CommunityCategory.ALL, // 선택된 카테고리 탭
    val searchQuery: String = "",                 // 검색창 입력값
    val errorMessage: String? = null              // 에러 발생 시 메시지
)

// ─────────────────────────────────────────
// 2. 게시글 상세 화면 상태
// ─────────────────────────────────────────
data class CommunityDetailUiState(
    val isLoading: Boolean = false,
    val post: CommunityPost? = null,              // null이면 아직 로딩 전
    val commentInput: String = "",                // 댓글 입력창 현재 값
    val isMenuExpanded: Boolean = false,          // 우측 상단 ⋮ 메뉴 열림 여부
    val errorMessage: String? = null
)

// ─────────────────────────────────────────
// 3. 게시글 작성 화면 상태
// ─────────────────────────────────────────
data class CommunityWriteUiState(
    val title: String = "",                       // 제목 입력값
    val selectedCategory: CommunityCategory? = null, // 카테고리 선택 (null = 미선택)
    val isCategoryDropdownExpanded: Boolean = false, // 드롭다운 열림 여부
    val contentBlocks: List<CommunityContentBlock> = emptyList(), // 본문 블록들
    val isSubmitting: Boolean = false,            // 작성완료 버튼 누른 후 서버 전송 중
    val isSubmitSuccess: Boolean = false,         // 작성 성공 → 화면 이동 트리거
    val errorMessage: String? = null
)

// ─────────────────────────────────────────
// 4. 게시글 수정 화면 상태
//    작성과 거의 같지만 기존 데이터를 초기값으로 로드
// ─────────────────────────────────────────
data class CommunityEditUiState(
    val isLoading: Boolean = false,               // 기존 게시글 불러오는 중
    val postId: Long? = null,
    val title: String = "",
    val selectedCategory: CommunityCategory? = null,
    val isCategoryDropdownExpanded: Boolean = false,
    val contentBlocks: List<CommunityContentBlock> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val errorMessage: String? = null
)
