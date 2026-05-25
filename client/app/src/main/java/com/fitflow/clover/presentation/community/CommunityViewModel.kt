package com.fitflow.clover.presentation.community

import androidx.lifecycle.ViewModel
import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.modal.CommunityReply
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommunityViewModel : ViewModel() {

    // ─────────────────────────────────────────────────────────
    // 게시판 목록 상태
    // ─────────────────────────────────────────────────────────
    private val _listUiState = MutableStateFlow(
        CommunityListUiState(posts = dummyPosts)
    )
    val listUiState: StateFlow<CommunityListUiState> = _listUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 게시글 상세 상태
    // ─────────────────────────────────────────────────────────
    private val _detailUiState = MutableStateFlow(
        CommunityDetailUiState(post = dummyPost)
    )
    val detailUiState: StateFlow<CommunityDetailUiState> = _detailUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 게시글 작성 상태
    // ─────────────────────────────────────────────────────────
    private val _writeUiState = MutableStateFlow(CommunityWriteUiState())
    val writeUiState: StateFlow<CommunityWriteUiState> = _writeUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 게시글 수정 상태
    // ─────────────────────────────────────────────────────────
    private val _editUiState = MutableStateFlow(CommunityEditUiState())
    val editUiState: StateFlow<CommunityEditUiState> = _editUiState.asStateFlow()

    // ─────────────────────────────────────────────────────────
    // 목록 화면 이벤트
    // ─────────────────────────────────────────────────────────

    // 카테고리 선택
    fun onCategorySelect(category: CommunityCategory) {
        _listUiState.value = _listUiState.value.copy(
            selectedCategory = category,
            posts = if (category == CommunityCategory.ALL) {
                dummyPosts
            } else {
                dummyPosts.filter { it.category == category }
            }
        )
    }

    // 검색어 입력
    fun onSearchQueryChange(query: String) {
        _listUiState.value = _listUiState.value.copy(
            searchQuery = query,
            posts = if (query.isEmpty()) {
                dummyPosts
            } else {
                dummyPosts.filter { it.title.contains(query) }
            }
        )
    }

    // ─────────────────────────────────────────────────────────
    // 상세 화면 이벤트
    // ─────────────────────────────────────────────────────────

    // 좋아요 토글
    fun onLikeClick() {
        val current = _detailUiState.value.post ?: return
        _detailUiState.value = _detailUiState.value.copy(
            post = current.copy(
                isLiked = !current.isLiked,
                likeCount = if (current.isLiked) current.likeCount - 1
                else current.likeCount + 1
            )
        )
    }

    // 더보기 메뉴 토글
    fun onMenuClick() {
        _detailUiState.value = _detailUiState.value.copy(
            isMenuExpanded = !_detailUiState.value.isMenuExpanded
        )
    }

    // 댓글 입력값 변경
    fun onCommentInputChange(input: String) {
        _detailUiState.value = _detailUiState.value.copy(commentInput = input)
    }

    // 댓글 등록
    fun onCommentSubmit() {
        val input = _detailUiState.value.commentInput
        if (input.isEmpty()) return

        val current = _detailUiState.value.post ?: return
        val newComment = CommunityComment(
            commentId = System.currentTimeMillis(),
            authorNickname = "나",
            authorProfileImageUrl = null,
            content = input,
            createdAt = "방금 전",
            isMyComment = true,
            replies = emptyList()
        )

        _detailUiState.value = _detailUiState.value.copy(
            post = current.copy(
                comments = current.comments + newComment
            ),
            commentInput = ""
        )
    }

    // ─────────────────────────────────────────────────────────
    // 작성 화면 이벤트
    // ─────────────────────────────────────────────────────────

    fun onWriteTitleChange(title: String) {
        _writeUiState.value = _writeUiState.value.copy(title = title)
    }

    fun onWriteCategorySelect(category: CommunityCategory) {
        _writeUiState.value = _writeUiState.value.copy(
            selectedCategory = category,
            isCategoryDropdownExpanded = false
        )
    }

    fun onWriteCategoryDropdownToggle(isExpanded: Boolean) {
        _writeUiState.value = _writeUiState.value.copy(
            isCategoryDropdownExpanded = isExpanded
        )
    }

    // 작성 완료
    fun onWriteSubmit() {
        val state = _writeUiState.value
        if (state.title.isEmpty() || state.selectedCategory == null) return

        _writeUiState.value = _writeUiState.value.copy(isSubmitSuccess = true)
    }

    // ─────────────────────────────────────────────────────────
    // 수정 화면 이벤트
    // ─────────────────────────────────────────────────────────

    fun onEditTitleChange(title: String) {
        _editUiState.value = _editUiState.value.copy(title = title)
    }

    fun onEditCategorySelect(category: CommunityCategory) {
        _editUiState.value = _editUiState.value.copy(
            selectedCategory = category,
            isCategoryDropdownExpanded = false
        )
    }

    fun onEditCategoryDropdownToggle(isExpanded: Boolean) {
        _editUiState.value = _editUiState.value.copy(
            isCategoryDropdownExpanded = isExpanded
        )
    }

    // 수정 완료
    fun onEditSubmit() {
        val state = _editUiState.value
        if (state.title.isEmpty() || state.selectedCategory == null) return

        _editUiState.value = _editUiState.value.copy(isSubmitSuccess = true)
    }
}

// ─────────────────────────────────────────────────────────
// 더미 데이터
// ─────────────────────────────────────────────────────────
private val dummyPosts = listOf(
    CommunityPostSummary(
        postId = 1L,
        category = CommunityCategory.FREE,
        title = "오늘 득템한 빈티지 자켓 어때요?",
        contentPreview = "동묘 앞에서 발견한 건데 상태가 진짜 좋더라고요",
        authorNickname = "야르",
        authorProfileImageUrl = null,
        likeCount = 24,
        commentCount = 8,
        createdAt = "1시간 전",
        thumbnailImageUrl = null
    ),
    CommunityPostSummary(
        postId = 2L,
        category = CommunityCategory.REVIEW,
        title = "무신사 구매 후기 솔직하게 씁니다",
        contentPreview = "배송은 빠른데 사이즈가 생각보다 크게 나왔어요",
        authorNickname = "코디왕",
        authorProfileImageUrl = null,
        likeCount = 11,
        commentCount = 3,
        createdAt = "3시간 전",
        thumbnailImageUrl = null
    ),
    CommunityPostSummary(
        postId = 3L,
        category = CommunityCategory.COORDINATION,
        title = "캐주얼 데일리룩 공유해요",
        contentPreview = "흰티에 와이드 팬츠 조합인데 생각보다 잘 어울려요",
        authorNickname = "패션피플",
        authorProfileImageUrl = null,
        likeCount = 37,
        commentCount = 12,
        createdAt = "5시간 전",
        thumbnailImageUrl = null
    )
)

private val dummyPost = CommunityPost(
    postId = 1L,
    category = CommunityCategory.FREE,
    title = "여기는 제목이 들어가야 하는 공간입니다",
    contentBlocks = listOf(
        CommunityContentBlock.TextBlock(
            "여기는 내용이 들어가는 공간이고 내용을 맘대로 작성하는 블로그 형식 느낌으로 디자인 적용해봤는데 어떤 가여"
        ),
        CommunityContentBlock.ImageBlock(
            imageUrl = "",
            description = "여기는 이미지 밑에 설명 들어갈 수 있게"
        ),
        CommunityContentBlock.TextBlock(
            "추가적으로 내용이 들어가면 이런 식으로 들어가서 글 작성하고, 내용 넣고 하면서 진행하면 괜찮을 듯요"
        )
    ),
    authorNickname = "야르",
    authorProfileImageUrl = null,
    likeCount = 1,
    isLiked = false,
    commentCount = 4,
    comments = listOf(
        CommunityComment(
            commentId = 1L,
            authorNickname = "DKDLEL",
            authorProfileImageUrl = null,
            content = "괜찮은 것 같아요 !!!! 좋은데요 ???",
            createdAt = "1시간 전",
            isMyComment = false,
            replies = emptyList()
        ),
        CommunityComment(
            commentId = 2L,
            authorNickname = "DKDLEL",
            authorProfileImageUrl = null,
            content = "괜찮은 것 같아요 !!!! 좋은데요 ???",
            createdAt = "2시간 전",
            isMyComment = false,
            replies = listOf(
                CommunityReply(
                    replyId = 1L,
                    authorNickname = "야늘자",
                    content = "괜찮은 것 같죠!! 이런 식으로 진행해도 괜찮을까요???",
                    createdAt = "1시간 전",
                    isMyReply = true
                )
            )
        )
    ),
    createdAt = "00시간 전",
    isMyPost = false
)
