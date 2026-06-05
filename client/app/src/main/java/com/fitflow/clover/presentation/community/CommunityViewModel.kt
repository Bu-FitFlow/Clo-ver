package com.fitflow.clover.presentation.community

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.modal.CommunityReply
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 커뮤니티 화면의 상태와 이벤트를 관리하는 ViewModel입니다.
 *
 * 현재 버전은 서버나 Repository가 연결되지 않은 상태에서도 화면 흐름을 테스트할 수 있도록
 * 메모리 기반 더미 데이터를 사용합니다. 추후 API가 준비되면 loadPosts(), loadPostDetail(),
 * submitPost(), submitEditPost(), deleteCurrentPost() 내부를 Repository 호출로 교체하면 됩니다.
 */
class CommunityViewModel : ViewModel() {

    // ─────────────────────────────────────────────────────────
    // 1. 화면별 UiState
    // ─────────────────────────────────────────────────────────

    private val _listUiState = MutableStateFlow(CommunityListUiState(isLoading = true))
    val listUiState: StateFlow<CommunityListUiState> = _listUiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(CommunityDetailUiState())
    val detailUiState: StateFlow<CommunityDetailUiState> = _detailUiState.asStateFlow()

    private val _writeUiState = MutableStateFlow(CommunityWriteUiState())
    val writeUiState: StateFlow<CommunityWriteUiState> = _writeUiState.asStateFlow()

    private val _editUiState = MutableStateFlow(CommunityEditUiState())
    val editUiState: StateFlow<CommunityEditUiState> = _editUiState.asStateFlow()

    /**
     * 목록 화면에서 사용하는 게시글 요약 데이터입니다.
     * 검색과 카테고리 필터의 원본 데이터로 사용됩니다.
     */
    private var allPosts: List<CommunityPostSummary> = emptyList()

    /**
     * 상세 화면에서 사용하는 게시글 상세 데이터입니다.
     * key는 postId입니다.
     */
    private var postDetails: Map<Long, CommunityPost> = emptyMap()

    init {
        loadPosts()
    }

    // ─────────────────────────────────────────────────────────
    // 2. 목록 화면 이벤트
    // ─────────────────────────────────────────────────────────

    fun loadPosts() {
        viewModelScope.launch {
            _listUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                delay(300)
                createDummyPosts()
            }.onSuccess { posts ->
                allPosts = posts
                postDetails = createDummyPostDetails(posts)

                _listUiState.update { current ->
                    current.copy(
                        isLoading = false,
                        posts = filterPosts(
                            posts = allPosts,
                            category = current.selectedCategory,
                            query = current.searchQuery
                        ),
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _listUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "게시글을 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    fun onCategorySelect(category: CommunityCategory) {
        _listUiState.update { current ->
            current.copy(
                selectedCategory = category,
                posts = filterPosts(
                    posts = allPosts,
                    category = category,
                    query = current.searchQuery
                )
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _listUiState.update { current ->
            current.copy(
                searchQuery = query,
                posts = filterPosts(
                    posts = allPosts,
                    category = current.selectedCategory,
                    query = query
                )
            )
        }
    }

    fun onWriteMenuToggle(isExpanded: Boolean? = null) {
        _listUiState.update { current ->
            current.copy(
                isWriteMenuExpanded = isExpanded ?: !current.isWriteMenuExpanded
            )
        }
    }

    // ─────────────────────────────────────────────────────────
    // 3. 상세 화면 이벤트
    // ─────────────────────────────────────────────────────────

    fun loadPostDetail(postId: Long) {
        viewModelScope.launch {
            _detailUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isMenuExpanded = false
                )
            }

            runCatching {
                delay(200)
                postDetails[postId] ?: error("게시글을 찾을 수 없습니다.")
            }.onSuccess { post ->
                _detailUiState.update {
                    it.copy(
                        isLoading = false,
                        post = post,
                        commentInput = "",
                        isMenuExpanded = false,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _detailUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "게시글 상세 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    fun onDetailMenuClick() {
        _detailUiState.update { current ->
            current.copy(isMenuExpanded = !current.isMenuExpanded)
        }
    }

    fun closeDetailMenu() {
        _detailUiState.update { it.copy(isMenuExpanded = false) }
    }

    fun onLikeClick() {
        _detailUiState.update { current ->
            val post = current.post ?: return@update current

            val nextPost = post.copy(
                isLiked = !post.isLiked,
                likeCount = if (post.isLiked) {
                    (post.likeCount - 1).coerceAtLeast(0)
                } else {
                    post.likeCount + 1
                }
            )

            postDetails = postDetails + (nextPost.postId to nextPost)
            updatePostSummaryLike(nextPost)

            current.copy(post = nextPost)
        }
    }

    fun onCommentInputChange(input: String) {
        _detailUiState.update { current ->
            current.copy(commentInput = input)
        }
    }

    fun onCommentSubmit() {
        _detailUiState.update { current ->
            val input = current.commentInput.trim()
            val post = current.post ?: return@update current

            if (input.isEmpty()) {
                return@update current
            }

            val newComment = CommunityComment(
                commentId = System.currentTimeMillis(),
                authorNickname = "나",
                authorProfileImageUrl = null,
                content = input,
                createdAt = "방금 전",
                isMyComment = true,
                replies = emptyList()
            )

            val nextPost = post.copy(
                comments = post.comments + newComment,
                commentCount = post.commentCount + 1
            )

            postDetails = postDetails + (nextPost.postId to nextPost)
            updatePostSummaryCommentCount(nextPost)

            current.copy(
                post = nextPost,
                commentInput = ""
            )
        }
    }

    fun onReplyClick(commentId: Long) {
        _detailUiState.update { current ->
            // 같은 댓글을 다시 누르면 입력창 닫기 (토글)
            val nextTargetId = if (current.replyTargetCommentId == commentId) null else commentId
            current.copy(
                replyTargetCommentId = nextTargetId,
                replyInput = ""
            )
        }
    }

    fun onReplyInputChange(input: String) {
        _detailUiState.update { current ->
            current.copy(replyInput = input)
        }
    }

    fun onReplySubmit() {
        _detailUiState.update { current ->
            val input = current.replyInput.trim()
            val post = current.post ?: return@update current
            val targetCommentId = current.replyTargetCommentId ?: return@update current

            if (input.isEmpty()) return@update current

            val newReply = CommunityReply(
                replyId = System.currentTimeMillis(),
                authorNickname = "나",
                content = input,
                createdAt = "방금 전",
                isMyReply = true
            )

            val nextPost = post.copy(
                comments = post.comments.map { comment ->
                    if (comment.commentId == targetCommentId) {
                        comment.copy(replies = comment.replies + newReply)
                    } else {
                        comment
                    }
                }
            )

            postDetails = postDetails + (nextPost.postId to nextPost)

            current.copy(
                post = nextPost,
                replyInput = "",
                replyTargetCommentId = null
            )
        }
    }

    fun onCommentDeleteClick(commentId: Long) {
        _detailUiState.update { current ->
            val post = current.post ?: return@update current
            val targetComment = post.comments.firstOrNull { it.commentId == commentId }

            if (targetComment == null || !targetComment.isMyComment) {
                return@update current
            }

            val nextPost = post.copy(
                comments = post.comments.filterNot { it.commentId == commentId },
                commentCount = (post.commentCount - 1).coerceAtLeast(0)
            )

            postDetails = postDetails + (nextPost.postId to nextPost)
            updatePostSummaryCommentCount(nextPost)

            current.copy(post = nextPost)
        }
    }

    // ─────────────────────────────────────────────────────────
    // 4. 작성 화면 이벤트
    // ─────────────────────────────────────────────────────────

    fun onWriteTitleChange(title: String) {
        _writeUiState.update { current ->
            current.copy(
                title = title,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onWriteCategorySelect(category: CommunityCategory) {
        _writeUiState.update { current ->
            current.copy(
                selectedCategory = normalizeWriteCategory(category),
                isCategoryDropdownExpanded = false,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onWriteCategoryDropdownToggle(isExpanded: Boolean) {
        _writeUiState.update { current ->
            current.copy(isCategoryDropdownExpanded = isExpanded)
        }
    }

    fun onWriteImageSelect(uri: Uri?) {
        _writeUiState.update { current ->
            current.copy(
                selectedImageUri = uri,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onWriteImageRemove() {
        _writeUiState.update { current ->
            current.copy(selectedImageUri = null)
        }
    }

    fun onWriteContentBlocksChange(blocks: List<CommunityContentBlock>) {
        _writeUiState.update { current ->
            current.copy(
                contentBlocks = blocks,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun submitPost(
        imageUri: Uri? = _writeUiState.value.selectedImageUri,
        onSuccess: (Long) -> Unit = {}
    ) {
        val current = _writeUiState.value
        val title = current.title.trim()

        if (title.isEmpty()) {
            _writeUiState.update {
                it.copy(errorMessage = "제목을 입력해 주세요.")
            }
            return
        }

        viewModelScope.launch {
            _writeUiState.update {
                it.copy(
                    isSubmitting = true,
                    isSubmitSuccess = false,
                    errorMessage = null
                )
            }

            runCatching {
                delay(300)

                val postId = System.currentTimeMillis()
                val finalImageUri = imageUri ?: current.selectedImageUri
                val imageBlock = finalImageUri?.let { uri ->
                    CommunityContentBlock.ImageBlock(
                        imageUrl = uri.toString(),
                        description = "첨부 이미지"
                    )
                }

                val contentBlocks = buildList {
                    addAll(current.contentBlocks)
                    if (imageBlock != null) {
                        add(imageBlock)
                    }
                    if (isEmpty()) {
                        add(CommunityContentBlock.TextBlock("내용을 입력해 주세요."))
                    }
                }

                val newPost = CommunityPost(
                    postId = postId,
                    category = normalizeWriteCategory(current.selectedCategory),
                    title = title,
                    contentBlocks = contentBlocks,
                    authorNickname = "나",
                    authorProfileImageUrl = null,
                    likeCount = 0,
                    isLiked = false,
                    commentCount = 0,
                    comments = emptyList(),
                    createdAt = "방금 전",
                    isMyPost = true
                )

                val newSummary = CommunityPostSummary(
                    postId = newPost.postId,
                    category = newPost.category,
                    title = newPost.title,
                    contentPreview = contentBlocks.firstTextOrDefault("새 게시글입니다."),
                    authorNickname = newPost.authorNickname,
                    authorProfileImageUrl = newPost.authorProfileImageUrl,
                    likeCount = newPost.likeCount,
                    commentCount = newPost.commentCount,
                    createdAt = newPost.createdAt,
                    thumbnailImageUrl = finalImageUri?.toString()
                )

                allPosts = listOf(newSummary) + allPosts
                postDetails = postDetails + (newPost.postId to newPost)

                newPost.postId
            }.onSuccess { postId ->
                _writeUiState.update {
                    CommunityWriteUiState(isSubmitSuccess = true)
                }
                refreshCurrentListFilter()
                onSuccess(postId)
            }.onFailure { throwable ->
                _writeUiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmitSuccess = false,
                        errorMessage = throwable.message ?: "게시글 등록에 실패했습니다."
                    )
                }
            }
        }
    }

    fun resetWriteState() {
        _writeUiState.update { CommunityWriteUiState() }
    }

    // ─────────────────────────────────────────────────────────
    // 5. 수정 화면 이벤트
    // ─────────────────────────────────────────────────────────

    fun startEdit(postId: Long) {
        val post = postDetails[postId]

        if (post == null) {
            _editUiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "수정할 게시글을 찾을 수 없습니다."
                )
            }
            return
        }

        _editUiState.update {
            it.copy(
                isLoading = false,
                postId = post.postId,
                title = post.title,
                selectedCategory = normalizeWriteCategory(post.category),
                isCategoryDropdownExpanded = false,
                selectedImageUri = null,
                contentBlocks = post.contentBlocks,
                isSubmitting = false,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onEditTitleChange(title: String) {
        _editUiState.update { current ->
            current.copy(
                title = title,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onEditCategorySelect(category: CommunityCategory) {
        _editUiState.update { current ->
            current.copy(
                selectedCategory = normalizeWriteCategory(category),
                isCategoryDropdownExpanded = false,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onEditCategoryDropdownToggle(isExpanded: Boolean) {
        _editUiState.update { current ->
            current.copy(isCategoryDropdownExpanded = isExpanded)
        }
    }

    fun onEditImageSelect(uri: Uri?) {
        _editUiState.update { current ->
            current.copy(
                selectedImageUri = uri,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun onEditImageRemove() {
        _editUiState.update { current ->
            current.copy(selectedImageUri = null)
        }
    }

    fun onEditContentBlocksChange(blocks: List<CommunityContentBlock>) {
        _editUiState.update { current ->
            current.copy(
                contentBlocks = blocks,
                isSubmitSuccess = false,
                errorMessage = null
            )
        }
    }

    fun submitEditPost(
        postId: Long? = _editUiState.value.postId,
        imageUri: Uri? = _editUiState.value.selectedImageUri,
        onSuccess: () -> Unit = {}
    ) {
        if (postId == null) {
            _editUiState.update {
                it.copy(errorMessage = "수정할 게시글 정보가 없습니다.")
            }
            return
        }

        val current = _editUiState.value
        val title = current.title.trim()

        if (title.isEmpty()) {
            _editUiState.update {
                it.copy(errorMessage = "제목을 입력해 주세요.")
            }
            return
        }

        viewModelScope.launch {
            _editUiState.update {
                it.copy(
                    isSubmitting = true,
                    isSubmitSuccess = false,
                    errorMessage = null
                )
            }

            runCatching {
                delay(300)

                val originPost = postDetails[postId] ?: error("수정할 게시글을 찾을 수 없습니다.")
                val finalImageUri = imageUri ?: current.selectedImageUri
                val imageBlock = finalImageUri?.let { uri ->
                    CommunityContentBlock.ImageBlock(
                        imageUrl = uri.toString(),
                        description = "수정 첨부 이미지"
                    )
                }

                val nextContentBlocks = buildList {
                    addAll(current.contentBlocks)
                    if (imageBlock != null) {
                        add(imageBlock)
                    }
                    if (isEmpty()) {
                        add(CommunityContentBlock.TextBlock("내용을 입력해 주세요."))
                    }
                }

                val editedPost = originPost.copy(
                    title = title,
                    category = normalizeWriteCategory(current.selectedCategory),
                    contentBlocks = nextContentBlocks
                )

                postDetails = postDetails + (postId to editedPost)
                allPosts = allPosts.map { summary ->
                    if (summary.postId == postId) {
                        summary.copy(
                            category = editedPost.category,
                            title = editedPost.title,
                            contentPreview = nextContentBlocks.firstTextOrDefault(summary.contentPreview),
                            thumbnailImageUrl = finalImageUri?.toString() ?: summary.thumbnailImageUrl
                        )
                    } else {
                        summary
                    }
                }

                editedPost
            }.onSuccess { editedPost ->
                _editUiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmitSuccess = true,
                        errorMessage = null
                    )
                }
                _detailUiState.update {
                    it.copy(
                        post = editedPost,
                        isMenuExpanded = false
                    )
                }
                refreshCurrentListFilter()
                onSuccess()
            }.onFailure { throwable ->
                _editUiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmitSuccess = false,
                        errorMessage = throwable.message ?: "게시글 수정에 실패했습니다."
                    )
                }
            }
        }
    }

    fun resetEditState() {
        _editUiState.update { CommunityEditUiState() }
    }

    // ─────────────────────────────────────────────────────────
    // 6. 삭제, 신고, 차단 이벤트
    // ─────────────────────────────────────────────────────────

    fun deleteCurrentPost(onSuccess: () -> Unit = {}) {
        val post = _detailUiState.value.post ?: return

        allPosts = allPosts.filterNot { it.postId == post.postId }
        postDetails = postDetails - post.postId

        _detailUiState.update { CommunityDetailUiState() }
        refreshCurrentListFilter()
        onSuccess()
    }

    fun onReportClick() {
        _detailUiState.update { current ->
            current.copy(
                isMenuExpanded = false,
                errorMessage = "신고 기능은 추후 서버 API와 연결할 예정입니다."
            )
        }
    }

    fun onBlockClick() {
        _detailUiState.update { current ->
            current.copy(
                isMenuExpanded = false,
                errorMessage = "차단 기능은 추후 서버 API와 연결할 예정입니다."
            )
        }
    }

    // ─────────────────────────────────────────────────────────
    // 7. 내부 유틸 함수
    // ─────────────────────────────────────────────────────────

    private fun refreshCurrentListFilter() {
        _listUiState.update { current ->
            current.copy(
                posts = filterPosts(
                    posts = allPosts,
                    category = current.selectedCategory,
                    query = current.searchQuery
                )
            )
        }
    }

    private fun filterPosts(
        posts: List<CommunityPostSummary>,
        category: CommunityCategory,
        query: String
    ): List<CommunityPostSummary> {
        return posts
            .filter { post ->
                category == CommunityCategory.ALL || post.category == category
            }
            .filter { post ->
                query.isBlank() ||
                        post.title.contains(query, ignoreCase = true) ||
                        post.contentPreview.contains(query, ignoreCase = true) ||
                        post.authorNickname.contains(query, ignoreCase = true)
            }
    }

    private fun updatePostSummaryLike(post: CommunityPost) {
        allPosts = allPosts.map { summary ->
            if (summary.postId == post.postId) {
                summary.copy(likeCount = post.likeCount)
            } else {
                summary
            }
        }
        refreshCurrentListFilter()
    }

    private fun updatePostSummaryCommentCount(post: CommunityPost) {
        allPosts = allPosts.map { summary ->
            if (summary.postId == post.postId) {
                summary.copy(commentCount = post.commentCount)
            } else {
                summary
            }
        }
        refreshCurrentListFilter()
    }

    /**
     * 작성/수정 화면에서는 ALL 카테고리로 글을 작성하지 않도록 보정합니다.
     */
    private fun normalizeWriteCategory(category: CommunityCategory): CommunityCategory {
        return if (category == CommunityCategory.ALL) {
            CommunityCategory.FREE
        } else {
            category
        }
    }

    private fun List<CommunityContentBlock>.firstTextOrDefault(defaultValue: String): String {
        return firstNotNullOfOrNull { block ->
            when (block) {
                is CommunityContentBlock.TextBlock -> block.text.takeIf { it.isNotBlank() }
                is CommunityContentBlock.ImageBlock -> null
            }
        } ?: defaultValue
    }

    // ─────────────────────────────────────────────────────────
    // 8. 더미 데이터
    // ─────────────────────────────────────────────────────────

    private fun createDummyPosts(): List<CommunityPostSummary> {
        return listOf(
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
                category = CommunityCategory.FREE,
                title = "중고 거래 처음인데 도움 주세요",
                contentPreview = "처음이라 어떻게 해야 할지 모르겠어요",
                authorNickname = "초보자",
                authorProfileImageUrl = null,
                likeCount = 5,
                commentCount = 3,
                createdAt = "2시간 전",
                thumbnailImageUrl = null
            ),
            CommunityPostSummary(
                postId = 3L,
                category = CommunityCategory.REVIEW,
                title = "구매 후기 솔직하게 씁니다",
                contentPreview = "배송은 빠른데 사이즈가 생각보다 크게 나왔어요",
                authorNickname = "코디왕",
                authorProfileImageUrl = null,
                likeCount = 11,
                commentCount = 3,
                createdAt = "3시간 전",
                thumbnailImageUrl = null
            ),
            CommunityPostSummary(
                postId = 4L,
                category = CommunityCategory.REVIEW,
                title = "빈티지샵 후기 남겨요",
                contentPreview = "퀄리티 대비 가격이 너무 좋아요",
                authorNickname = "리뷰어",
                authorProfileImageUrl = null,
                likeCount = 18,
                commentCount = 6,
                createdAt = "4시간 전",
                thumbnailImageUrl = null
            ),
            CommunityPostSummary(
                postId = 5L,
                category = CommunityCategory.COORDINATION,
                title = "캐주얼 데일리룩 공유해요",
                contentPreview = "흰티에 와이드 팬츠 조합인데 생각보다 잘 어울려요",
                authorNickname = "패션피플",
                authorProfileImageUrl = null,
                likeCount = 37,
                commentCount = 12,
                createdAt = "5시간 전",
                thumbnailImageUrl = null
            ),
            CommunityPostSummary(
                postId = 6L,
                category = CommunityCategory.COORDINATION,
                title = "겨울 코디 추천해요",
                contentPreview = "패딩에 청바지 조합 어떤가요?",
                authorNickname = "스타일리스트",
                authorProfileImageUrl = null,
                likeCount = 22,
                commentCount = 9,
                createdAt = "6시간 전",
                thumbnailImageUrl = null
            )
        )
    }

    private fun createDummyPostDetails(posts: List<CommunityPostSummary>): Map<Long, CommunityPost> {
        return posts.associate { summary ->
            summary.postId to CommunityPost(
                postId = summary.postId,
                category = summary.category,
                title = summary.title,
                contentBlocks = when (summary.postId) {
                    // 이미지 있는 글
                    1L -> listOf(
                        CommunityContentBlock.TextBlock(summary.contentPreview),
                        CommunityContentBlock.ImageBlock(imageUrl = "", description = "이미지"),
                        CommunityContentBlock.TextBlock("거래 경험이나 코디에 대한 의견을 자유롭게 나눠 주세요.")
                    )
                    3L -> listOf(
                        CommunityContentBlock.TextBlock(summary.contentPreview),
                        CommunityContentBlock.ImageBlock(imageUrl = "", description = "상품 이미지"),
                        CommunityContentBlock.TextBlock("사이즈 표랑 실제 착용감이 좀 달랐어요.")
                    )
                    // 이미지 없는 글
                    else -> listOf(
                        CommunityContentBlock.TextBlock(summary.contentPreview),
                        CommunityContentBlock.TextBlock("거래 경험이나 코디에 대한 의견을 자유롭게 나눠 주세요.")
                    )
                },
                authorNickname = summary.authorNickname,
                authorProfileImageUrl = summary.authorProfileImageUrl,
                likeCount = summary.likeCount,
                isLiked = false,
                commentCount = summary.commentCount,
                comments = createDummyComments(),
                createdAt = summary.createdAt,
                isMyPost = summary.postId == 1L
            )
        }
    }

    private fun createDummyComments(): List<CommunityComment> {
        return listOf(
            CommunityComment(
                commentId = 1L,
                authorNickname = "나나",
                authorProfileImageUrl = null,
                content = "괜찮은 것 같아요 !!!! 좋은데요 ???",
                createdAt = "1시간 전",
                isMyComment = false,
                replies = emptyList()
            ),
            CommunityComment(
                commentId = 2L,
                authorNickname = "야늘자",
                authorProfileImageUrl = null,
                content = "저도 비슷한 제품 거래해 봤는데 상태 확인만 잘하면 괜찮아요.",
                createdAt = "2시간 전",
                isMyComment = false,
                replies = listOf(
                    CommunityReply(
                        replyId = 1L,
                        authorNickname = "나",
                        content = "좋은 의견 감사합니다!",
                        createdAt = "방금 전",
                        isMyReply = true
                    )
                )
            )
        )
    }
}