package com.fitflow.clover.presentation.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.fitflow.clover.R
import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.modal.CommunityReply

private val CloverGreen = Color(0xFF99DE81)

// ─────────────────────────────────────────────────────────
// 1. 게시판 목록 화면
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityListScreen(
    uiState: CommunityListUiState = CommunityListUiState(),
    onPostClick: (Long) -> Unit = {},
    onWriteClick: () -> Unit = {},
    onCategorySelect: (CommunityCategory) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onMenuClick: (Long) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    // 뒤로가기 + 타이틀
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_icon),
                                contentDescription = "뒤로가기",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { onBackClick() }
                                    .padding(12.dp)
                            )
                        }
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "커뮤니티",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.size(48.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // 검색창
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            placeholder = { Text("검색어를 입력하세요", fontSize = 14.sp) },
                            trailingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                focusedBorderColor = CloverGreen,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 카테고리 탭
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(36.dp)
                        ) {
                            CommunityCategory.entries.forEach { category ->
                                CategoryChip(
                                    category = category,
                                    isSelected = uiState.selectedCategory == category,
                                    onClick = { onCategorySelect(category) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    HorizontalDivider(color = Color.Black, thickness = 1.dp)
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onWriteClick,
                    containerColor = CloverGreen,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "글쓰기",
                        tint = Color.White
                    )
                }
            }
        ) { paddingValues ->
            when {
                // 로딩 중
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CloverGreen)
                    }
                }
                // 에러
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                // 글 목록
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(uiState.posts) { post ->
                            PostItem(
                                post = post,
                                onPostClick = onPostClick,
                                onMenuClick = onMenuClick
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 2. 게시글 상세 화면
// ─────────────────────────────────────────────────────────
@Composable
fun CommunityDetailScreen(
    uiState: CommunityDetailUiState = CommunityDetailUiState(),
    onBackClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onCommentInputChange: (String) -> Unit = {},
    onCommentSubmit: () -> Unit = {},
    onReplyClick: (Long) -> Unit = {},
    onCommentDeleteClick: (Long) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    onBlockClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CommunityCloverTopBar(onBackClick = onBackClick)
            }
        ) { paddingValues ->
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CloverGreen)
                    }
                }

                uiState.post != null -> {
                    val post = uiState.post
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        // 게시글 본문 카드
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // 제목
                                    Text(
                                        text = post.title,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 16.dp,
                                            bottom = 8.dp
                                        )
                                    )

                                    // 작성자 정보
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // 프로필 이미지 placeholder
                                        Surface(
                                            modifier = Modifier.size(28.dp),
                                            shape = CircleShape,
                                            color = Color.LightGray
                                        ) {}

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = post.authorNickname,
                                            fontSize = 13.sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "  ${post.createdAt}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            text = post.category.displayName,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = Color.LightGray)

                                    // 본문 블록 렌더링
                                    post.contentBlocks.forEach { block ->
                                        ContentBlockItem(block = block)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // 좋아요 버튼 + 더보기 메뉴
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        LikeButton(
                                            likeCount = post.likeCount,
                                            isLiked = post.isLiked,
                                            onClick = onLikeClick
                                        )

                                        Box {
                                            IconButton(onClick = onMenuClick) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = "더보기",
                                                    tint = Color.Gray
                                                )
                                            }
                                            if (uiState.isMenuExpanded) {
                                                PostMenuPopup(
                                                    isMyPost = post.isMyPost,
                                                    onEditClick = onEditClick,
                                                    onDeleteClick = onDeleteClick,
                                                    onReportClick = onReportClick,
                                                    onBlockClick = onBlockClick,
                                                    onDismiss = onMenuClick
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 댓글 섹션
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F8E0)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column {
                                    // 댓글 탭 헤더
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 16.dp, top = 8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(
                                                topStart = 8.dp,
                                                topEnd = 8.dp
                                            ),
                                            color = Color.White
                                        ) {
                                            Text(
                                                text = "댓글",
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 6.dp
                                                )
                                            )
                                        }
                                    }

                                    // 댓글 목록
                                    post.comments.forEach { comment ->
                                        CommentItem(
                                            comment = comment,
                                            onReplyClick = onReplyClick,
                                            onDeleteClick = onCommentDeleteClick
                                        )
                                    }

                                    // 댓글 입력창
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = uiState.commentInput,
                                            onValueChange = onCommentInputChange,
                                            modifier = Modifier.weight(1f),
                                            placeholder = { Text("댓글", fontSize = 13.sp) },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = Color.Transparent,
                                                focusedBorderColor = CloverGreen,
                                                unfocusedContainerColor = Color.White,
                                                focusedContainerColor = Color.White
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        TextButton(onClick = onCommentSubmit) {
                                            Text("등록", color = CloverGreen)
                                        }
                                    }
                                }
                            }
                        }

                        // 하단 여백
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 3. 게시글 작성 화면
// ─────────────────────────────────────────────────────────
@Composable
fun CommunityWriteScreen(
    uiState: CommunityWriteUiState = CommunityWriteUiState(),
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onCategorySelect: (CommunityCategory) -> Unit = {},
    onCategoryDropdownToggle: (Boolean) -> Unit = {},
    onImagePickClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    CommunityCloverTopBar(onBackClick = onBackClick)

                    // 제목 입력 + 등록 버튼
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "제목:",
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTitleInput(
                                value = uiState.title,
                                onValueChange = onTitleChange
                            )
                        }
                        TextButton(
                            onClick = onSubmitClick,
                            enabled = !uiState.isSubmitting
                        ) {
                            Text(
                                text = "등록",
                                color = if (uiState.isSubmitting) Color.Gray else CloverGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray)

                    // 카테고리 선택 + 이미지 추가 버튼
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CategoryDropdown(
                            selectedCategory = uiState.selectedCategory,
                            isExpanded = uiState.isCategoryDropdownExpanded,
                            onExpandChange = onCategoryDropdownToggle,
                            onCategorySelect = onCategorySelect
                        )

                        IconButton(onClick = onImagePickClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "이미지 추가",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray)
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.contentBlocks) { block ->
                    ContentBlockItem(block = block)
                }
                item { Spacer(modifier = Modifier.height(200.dp)) }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 4. 게시글 수정 화면
// ─────────────────────────────────────────────────────────
@Composable
fun CommunityEditScreen(
    uiState: CommunityEditUiState = CommunityEditUiState(),
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onCategorySelect: (CommunityCategory) -> Unit = {},
    onCategoryDropdownToggle: (Boolean) -> Unit = {},
    onImagePickClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    CommunityCloverTopBar(onBackClick = onBackClick)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "제목:",
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTitleInput(
                                value = uiState.title,
                                onValueChange = onTitleChange
                            )
                        }
                        // 작성 화면과 다른 점: "수정" 버튼
                        TextButton(
                            onClick = onSubmitClick,
                            enabled = !uiState.isSubmitting
                        ) {
                            Text(
                                text = "수정",
                                color = if (uiState.isSubmitting) Color.Gray else CloverGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CategoryDropdown(
                            selectedCategory = uiState.selectedCategory,
                            isExpanded = uiState.isCategoryDropdownExpanded,
                            onExpandChange = onCategoryDropdownToggle,
                            onCategorySelect = onCategorySelect
                        )

                        IconButton(onClick = onImagePickClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "이미지 추가",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray)
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.contentBlocks) { block ->
                    ContentBlockItem(block = block)
                }
                item { Spacer(modifier = Modifier.height(200.dp)) }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 공통 - 로고 상단바 (상세/작성/수정 화면)
// ─────────────────────────────────────────────────────────
@JvmOverloads
@Composable
fun CommunityCloverTopBar(
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F8E0))
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "뒤로가기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onBackClick() }
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Clo-ver 로고",
                modifier = Modifier.width(58.dp)
                    .height(45.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.size(48.dp))
    }
}

// ─────────────────────────────────────────────────────────
// 공통 - 제목 입력 필드 (테두리 없는 스타일)
// ─────────────────────────────────────────────────────────
@Composable
fun BasicTitleInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color.Black
        ),
        decorationBox = { innerTextField: @Composable () -> Unit ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = "제목을 입력하세요",
                        fontSize = 15.sp,
                        color = Color.LightGray
                    )
                }
                innerTextField()
            }
        }
    )
}

// ─────────────────────────────────────────────────────────
// Preview 더미 데이터
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
    title = "제목이 들어가는 공간입니다",
    contentBlocks = listOf(
        CommunityContentBlock.TextBlock(
            "내용"
        ),
        CommunityContentBlock.ImageBlock(
            imageUrl = "",
            description = " 이미지 "
        ),
        CommunityContentBlock.TextBlock(
            "내용"
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

// ─────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityListPreview() {
    CommunityListScreen(
        uiState = CommunityListUiState(posts = dummyPosts)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityDetailPreview() {
    CommunityDetailScreen(
        uiState = CommunityDetailUiState(post = dummyPost)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityWritePreview() {
    var uiState by remember {
        mutableStateOf(CommunityWriteUiState())
    }
    CommunityWriteScreen(
        uiState = uiState,
        onCategorySelect = { category ->
            uiState = uiState.copy(
                selectedCategory = category,
                isCategoryDropdownExpanded = false
            )
        },
        onCategoryDropdownToggle = { isExpanded ->
            uiState = uiState.copy(isCategoryDropdownExpanded = isExpanded)
        },
        onTitleChange = { title ->
            uiState = uiState.copy(title = title)
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityEditPreview() {
    var uiState by remember {
        mutableStateOf(CommunityEditUiState())
    }
    CommunityEditScreen(
        uiState = uiState,
        onCategorySelect = { category ->
            uiState = uiState.copy(
                selectedCategory = category,
                isCategoryDropdownExpanded = false
            )
        },
        onCategoryDropdownToggle = { isExpanded ->
            uiState = uiState.copy(isCategoryDropdownExpanded = isExpanded)
        },
        onTitleChange = { title ->
            uiState = uiState.copy(title = title)
        }
    )
}