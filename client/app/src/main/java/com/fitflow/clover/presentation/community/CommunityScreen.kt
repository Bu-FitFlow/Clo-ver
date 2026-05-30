package com.fitflow.clover.presentation.community

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitflow.clover.R
import com.fitflow.clover.domain.modal.*

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
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onProductListClick: () -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onMyPageClick: () -> Unit = {},
    onSellClick: () -> Unit = {}
) {
    // 1. 메뉴 상태 관리 변수 추가
    var isMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CommunityCloverTopBar(onBackClick = onBackClick)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("검색어를 입력하세요", fontSize = 13.sp) },
                            trailingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Black,
                                focusedBorderColor = CloverGreen,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 게시글 리스트 영역
                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = CloverGreen)
                        }
                    }
                    uiState.errorMessage != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = uiState.errorMessage, color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(uiState.posts) { post ->
                                PostItem(post = post, onPostClick = onPostClick, onMenuClick = onMenuClick)
                            }
                        }
                    }
                }

                // 2. 드롭다운 메뉴 레이아웃 (열려있을 때만 표시)
                if (isMenuExpanded) {
                    // 배경 클릭 시 닫히도록 하는 투명 레이어
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { isMenuExpanded = false }
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 64.dp), // 버튼 위쪽에 위치하도록 bottom 여백 조절
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 상단 메뉴 박스
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.width(140.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                MenuItem("알림") {
                                    isMenuExpanded = false
                                    onNotificationClick()
                                }
                                MenuItem("채팅방") {
                                    isMenuExpanded = false
                                    onChatClick()
                                }
                                MenuItem("판매글") {
                                    isMenuExpanded = false
                                    onProductListClick()
                                }
                                MenuItem("커뮤니티") {
                                    isMenuExpanded = false
                                    onCommunityClick()
                                }
                                MenuItem("마이페이지") {
                                    isMenuExpanded = false
                                    onMyPageClick()
                                }
                            }
                        }

                        // 하단 메뉴 박스
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.width(140.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                MenuItem("판매") {
                                    isMenuExpanded = false
                                    onSellClick()
                                }
                                MenuItem("글쓰기") {
                                    isMenuExpanded = false
                                    onWriteClick()
                                }
                            }
                        }
                    }
                }

                // 3. 하단 오른쪽 버튼
                Image(
                    painter = painterResource(id = R.drawable.listbar),
                    contentDescription = "메뉴 열기",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                        .size(56.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            isMenuExpanded = !isMenuExpanded // 클릭 시 메뉴 토글
                        }
                )
            }
        }
    }
}

// 메뉴 아이템 디자인을 위한 보조 컴포저블
@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
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
    onReplyInputChange: (String) -> Unit = {},
    onReplySubmit: () -> Unit = {},
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
                                    Text(
                                        text = post.title,
                                        fontSize = 20.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 16.dp,
                                            bottom = 8.dp
                                        )
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            modifier = Modifier.size(28.dp),
                                            shape = CircleShape,
                                            color = Color.LightGray
                                        ) {}

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = post.authorNickname,
                                            fontSize = 10.sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "  ${post.createdAt}",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            text = post.category.displayName,
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = Color.LightGray)

                                    post.contentBlocks.forEach { block ->
                                        ContentBlockItem(block = block)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

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

                                    post.comments.forEach { comment ->
                                        CommentItem(
                                            comment = comment,
                                            onReplyClick = onReplyClick,
                                            onDeleteClick = onCommentDeleteClick
                                        )
                                        // 대댓글 입력창: 해당 댓글의 "답글" 버튼을 눌렀을 때만 표시
                                        if (uiState.replyTargetCommentId == comment.commentId) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 28.dp, end = 8.dp, bottom = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                OutlinedTextField(
                                                    value = uiState.replyInput,
                                                    onValueChange = onReplyInputChange,
                                                    modifier = Modifier.weight(1f),
                                                    placeholder = { Text("대댓글 입력...", fontSize = 11.sp) },
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        unfocusedBorderColor = Color.LightGray,
                                                        focusedBorderColor = CloverGreen,
                                                        unfocusedContainerColor = Color.White,
                                                        focusedContainerColor = Color.White
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                TextButton(onClick = onReplySubmit) {
                                                    Text("등록", color = CloverGreen, fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }

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

                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 3. 게시글 작성 화면 (파라미터 이름: onCompleteClick)
// ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityWriteScreen(
    uiState: CommunityWriteUiState = CommunityWriteUiState(),
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onCategorySelect: (CommunityCategory) -> Unit = {},
    onCategoryDropdownToggle: (Boolean) -> Unit = {},
    onContentBlocksChange: (List<CommunityContentBlock>) -> Unit = {},
    onCompleteClick: (Uri?) -> Unit = {} // 이름 통일: onCompleteClick
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) selectedImageUri = uri }
    )

    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) pendingCameraUri?.let { selectedImageUri = it }
            pendingCameraUri = null
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                val values = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "community_${System.currentTimeMillis()}.jpg")
                    put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val uri = context.contentResolver.insert(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
                if (uri != null) {
                    pendingCameraUri = uri
                    cameraLauncher.launch(uri)
                }
            }
        }
    )

    var showImageSourceDialog by remember { mutableStateOf(false) }

    if (showImageSourceDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("이미지 추가") },
            text = { Text("이미지를 어떻게 추가할까요?") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showImageSourceDialog = false
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }) { Text("카메라로 찍기") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showImageSourceDialog = false
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("갤러리에서 선택") }
            }
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                    CommunityCloverTopBar(onBackClick = onBackClick)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Text(text = "제목:", fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTitleInput(value = uiState.title, onValueChange = onTitleChange)
                        }
                        TextButton(
                            onClick = { onCompleteClick(selectedImageUri) },
                            enabled = !uiState.isSubmitting
                        ) {
                            Text(text = "완료", color = if (uiState.isSubmitting) Color.Gray else CloverGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                    HorizontalDivider(color = Color.LightGray)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CategoryDropdown(
                            selectedCategory = uiState.selectedCategory,
                            isExpanded = uiState.isCategoryDropdownExpanded,
                            onExpandChange = onCategoryDropdownToggle,
                            onCategorySelect = onCategorySelect
                        )
                        IconButton(onClick = { showImageSourceDialog = true }) {
                            Icon(painter = painterResource(id = R.drawable.image), contentDescription = "이미지 추가", modifier = Modifier.size(28.dp))
                        }
                    }
                    HorizontalDivider(color = Color.LightGray)
                }
            }
        ) { paddingValues ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                // 이미지 미리보기
                selectedImageUri?.let { uri ->
                    Box(modifier = Modifier.padding(vertical = 12.dp)) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { selectedImageUri = null },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "삭제", tint = Color.White)
                        }
                    }
                }

                // ── 본문 입력창 ──
                val textValue = uiState.contentBlocks
                    .filterIsInstance<CommunityContentBlock.TextBlock>()
                    .joinToString("\n") { it.text }
                BasicTextField(
                    value = textValue,
                    onValueChange = { newText ->
                        val imageBlocks = uiState.contentBlocks.filterIsInstance<CommunityContentBlock.ImageBlock>()
                        val newBlocks = buildList {
                            if (newText.isNotEmpty()) add(CommunityContentBlock.TextBlock(newText))
                            addAll(imageBlocks)
                        }
                        onContentBlocksChange(newBlocks)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 300.dp)
                        .padding(vertical = 8.dp),
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black, lineHeight = 22.sp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (textValue.isEmpty()) {
                                Text("내용을 입력하세요", fontSize = 14.sp, color = Color.LightGray)
                            }
                            innerTextField()
                        }
                    }
                )

                // 이미지 블록 렌더링
                uiState.contentBlocks.filterIsInstance<CommunityContentBlock.ImageBlock>().forEach { block ->
                    ContentBlockItem(block = block)
                }

                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }
}

//4.게시글 수정 화면
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityEditScreen(
    uiState: CommunityEditUiState = CommunityEditUiState(),
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onCategorySelect: (CommunityCategory) -> Unit = {},
    onCategoryDropdownToggle: (Boolean) -> Unit = {},
    onContentBlocksChange: (List<CommunityContentBlock>) -> Unit = {},
    onSubmitClick: (Uri?) -> Unit = {} // 수정 완료 시 Uri를 전달하도록 설정
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) selectedImageUri = uri }
    )

    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) pendingCameraUri?.let { selectedImageUri = it }
            pendingCameraUri = null
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                val values = android.content.ContentValues().apply {
                    put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "community_${System.currentTimeMillis()}.jpg")
                    put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val uri = context.contentResolver.insert(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
                if (uri != null) {
                    pendingCameraUri = uri
                    cameraLauncher.launch(uri)
                }
            }
        }
    )

    var showImageSourceDialog by remember { mutableStateOf(false) }

    if (showImageSourceDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("이미지 추가") },
            text = { Text("이미지를 어떻게 추가할까요?") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showImageSourceDialog = false
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }) { Text("카메라로 찍기") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showImageSourceDialog = false
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("갤러리에서 선택") }
            }
        )
    }

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
                    // 공통 상단바 (로고 등)
                    CommunityCloverTopBar(onBackClick = onBackClick)

                    // 제목 입력 및 수정 버튼 영역
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

                        // 3. 수정(완료) 버튼: 클릭 시 선택된 이미지 URI를 넘겨줌
                        TextButton(
                            onClick = { onSubmitClick(selectedImageUri) },
                            enabled = !uiState.isSubmitting && uiState.title.isNotEmpty()
                        ) {
                            Text(
                                text = "수정",
                                color = if (uiState.isSubmitting) Color.Gray else CloverGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = Color.LightGray)

                    // 카테고리 선택 및 이미지 첨부 버튼 영역
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

                        // 4. 이미지 추가 아이콘 버튼
                        IconButton(onClick = { showImageSourceDialog = true }) {
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
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                // 새로 선택된 이미지 미리보기
                selectedImageUri?.let { uri ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "수정 첨부 이미지",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            onClick = { selectedImageUri = null },
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "삭제",
                                tint = Color.White,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

                // ── 본문 입력창 ──
                val textValue = uiState.contentBlocks
                    .filterIsInstance<CommunityContentBlock.TextBlock>()
                    .joinToString("\n") { it.text }
                BasicTextField(
                    value = textValue,
                    onValueChange = { newText ->
                        val imageBlocks = uiState.contentBlocks.filterIsInstance<CommunityContentBlock.ImageBlock>()
                        val newBlocks = buildList {
                            if (newText.isNotEmpty()) add(CommunityContentBlock.TextBlock(newText))
                            addAll(imageBlocks)
                        }
                        onContentBlocksChange(newBlocks)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 300.dp)
                        .padding(vertical = 8.dp),
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black, lineHeight = 22.sp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (textValue.isEmpty()) {
                                Text("내용을 입력하세요", fontSize = 14.sp, color = Color.LightGray)
                            }
                            innerTextField()
                        }
                    }
                )

                // 이미지 블록 렌더링
                uiState.contentBlocks.filterIsInstance<CommunityContentBlock.ImageBlock>().forEach { block ->
                    ContentBlockItem(block = block)
                }

                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }
}

// 공통 - 로고 상단바 (피그마 기준 W393, H62 수정)
// ─────────────────────────────────────────────────────────
@Composable
fun CommunityCloverTopBar(
    onBackClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 상태바 영역 - 흰색
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .background(Color.White)
        )
        // 상단바 - 녹색, 고정 높이 57dp
        Surface(
            color = Color(0xFFE8F8E0),
            modifier = Modifier
                .fillMaxWidth()
                .height(57.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Clo-ver 로고",
                        modifier = Modifier
                            .width(80.dp)
                            .height(62.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    }
}



// ─────────────────────────────────────────────────────────
// 공통 - 제목 입력 필드
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

private val dummyPost = CommunityPost(
    postId = 1L,
    category = CommunityCategory.FREE,
    title = "제목이 들어가는 공간입니다",
    contentBlocks = listOf(
        CommunityContentBlock.TextBlock("내용"),
        CommunityContentBlock.ImageBlock(imageUrl = "", description = "이미지"),
        CommunityContentBlock.TextBlock("내용")
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
// Preview 화면 상태 관리 (중복 제거됨)
// ─────────────────────────────────────────────────────────
enum class CommunityPreviewScreen {
    LIST, DETAIL, WRITE, EDIT
}

// ─────────────────────────────────────────────────────────
// Preview - 목록/상세/작성/수정 화면 전환 통합 테스트
// ─────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityListPreview() {
    var currentScreen by remember { mutableStateOf(CommunityPreviewScreen.LIST) }
    var listUiState by remember { mutableStateOf(CommunityListUiState(posts = dummyPosts)) }
    var detailUiState by remember { mutableStateOf(CommunityDetailUiState(post = dummyPost)) }
    var writeUiState by remember { mutableStateOf(CommunityWriteUiState()) }
    var editUiState by remember { mutableStateOf(CommunityEditUiState()) }

    when (currentScreen) {
        CommunityPreviewScreen.LIST -> {
            CommunityListScreen(
                uiState = listUiState,
                onPostClick = { currentScreen = CommunityPreviewScreen.DETAIL },
                onWriteClick = { currentScreen = CommunityPreviewScreen.WRITE },
                onCategorySelect = { category ->
                    listUiState = listUiState.copy(
                        selectedCategory = category,
                        posts = if (category == CommunityCategory.ALL) dummyPosts
                        else dummyPosts.filter { it.category == category }
                    )
                },
                onSearchQueryChange = { query ->
                    listUiState = listUiState.copy(
                        searchQuery = query,
                        posts = if (query.isEmpty()) dummyPosts
                        else dummyPosts.filter { it.title.contains(query) }
                    )
                }
            )
        }

        CommunityPreviewScreen.DETAIL -> {
            CommunityDetailScreen(
                uiState = detailUiState,
                onBackClick = { currentScreen = CommunityPreviewScreen.LIST },
                onEditClick = { currentScreen = CommunityPreviewScreen.EDIT },
                onLikeClick = {
                    val current = detailUiState.post ?: return@CommunityDetailScreen
                    detailUiState = detailUiState.copy(
                        post = current.copy(
                            isLiked = !current.isLiked,
                            likeCount = if (current.isLiked) current.likeCount - 1
                            else current.likeCount + 1
                        )
                    )
                },
                onCommentInputChange = { input ->
                    detailUiState = detailUiState.copy(commentInput = input)
                },
                onCommentSubmit = {
                    val input = detailUiState.commentInput
                    if (input.isEmpty()) return@CommunityDetailScreen
                    val current = detailUiState.post ?: return@CommunityDetailScreen
                    detailUiState = detailUiState.copy(
                        post = current.copy(
                            comments = current.comments + CommunityComment(
                                commentId = System.currentTimeMillis(),
                                authorNickname = "나",
                                authorProfileImageUrl = null,
                                content = input,
                                createdAt = "방금 전",
                                isMyComment = true,
                                replies = emptyList()
                            )
                        ),
                        commentInput = ""
                    )
                },
                onReplyClick = { commentId ->
                    val nextId = if (detailUiState.replyTargetCommentId == commentId) null else commentId
                    detailUiState = detailUiState.copy(replyTargetCommentId = nextId, replyInput = "")
                },
                onReplyInputChange = { input ->
                    detailUiState = detailUiState.copy(replyInput = input)
                },
                onReplySubmit = {
                    val input = detailUiState.replyInput.trim()
                    val targetId = detailUiState.replyTargetCommentId ?: return@CommunityDetailScreen
                    val current = detailUiState.post ?: return@CommunityDetailScreen
                    if (input.isEmpty()) return@CommunityDetailScreen
                    val newReply = CommunityReply(
                        replyId = System.currentTimeMillis(),
                        authorNickname = "나",
                        content = input,
                        createdAt = "방금 전",
                        isMyReply = true
                    )
                    detailUiState = detailUiState.copy(
                        post = current.copy(
                            comments = current.comments.map { c ->
                                if (c.commentId == targetId) c.copy(replies = c.replies + newReply) else c
                            }
                        ),
                        replyInput = "",
                        replyTargetCommentId = null
                    )
                }
            )
        }

        CommunityPreviewScreen.WRITE -> {
            CommunityWriteScreen(
                uiState = writeUiState,
                onBackClick = { currentScreen = CommunityPreviewScreen.LIST },
                onTitleChange = { writeUiState = writeUiState.copy(title = it) },
                onCategorySelect = { category ->
                    writeUiState = writeUiState.copy(
                        selectedCategory = category,
                        isCategoryDropdownExpanded = false
                    )
                },
                onCategoryDropdownToggle = { isExpanded ->
                    writeUiState = writeUiState.copy(isCategoryDropdownExpanded = isExpanded)
                },
                onCompleteClick = { _ -> // Uri 파라미터 대응
                    currentScreen = CommunityPreviewScreen.LIST
                },
                onContentBlocksChange = { blocks ->
                    writeUiState = writeUiState.copy(contentBlocks = blocks)
                }
            )
        }

        CommunityPreviewScreen.EDIT -> {
            CommunityEditScreen(
                uiState = editUiState,
                onBackClick = { currentScreen = CommunityPreviewScreen.DETAIL },
                onTitleChange = { editUiState = editUiState.copy(title = it) },
                onCategorySelect = { category ->
                    editUiState = editUiState.copy(
                        selectedCategory = category,
                        isCategoryDropdownExpanded = false
                    )
                },
                onCategoryDropdownToggle = { isExpanded ->
                    editUiState = editUiState.copy(isCategoryDropdownExpanded = isExpanded)
                },
                onSubmitClick = { _ -> // Uri 파라미터 대응
                    currentScreen = CommunityPreviewScreen.DETAIL
                },
                onContentBlocksChange = { blocks ->
                    editUiState = editUiState.copy(contentBlocks = blocks)
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 개별 화면 Preview (함수명 중복 해결)
// ─────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityDetailPreview() {
    CommunityDetailScreen(
        uiState = CommunityDetailUiState(
            post = dummyPost,
            isMenuExpanded = true
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityWritePreview() {
    var uiState by remember { mutableStateOf(CommunityWriteUiState()) }
    CommunityWriteScreen(
        uiState = uiState,
        onTitleChange = { uiState = uiState.copy(title = it) },
        onCategorySelect = { category ->
            uiState = uiState.copy(selectedCategory = category, isCategoryDropdownExpanded = false)
        },
        onCategoryDropdownToggle = { isExpanded ->
            uiState = uiState.copy(isCategoryDropdownExpanded = isExpanded)
        },
        onContentBlocksChange = { blocks -> uiState = uiState.copy(contentBlocks = blocks) },
        onCompleteClick = { _ -> } // 필수 파라미터 추가
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityEditPreview() {
    var uiState by remember { mutableStateOf(CommunityEditUiState()) }
    CommunityEditScreen(
        uiState = uiState,
        onTitleChange = { uiState = uiState.copy(title = it) },
        onCategorySelect = { category ->
            uiState = uiState.copy(selectedCategory = category, isCategoryDropdownExpanded = false)
        },
        onCategoryDropdownToggle = { isExpanded ->
            uiState = uiState.copy(isCategoryDropdownExpanded = isExpanded)
        },
        onContentBlocksChange = { blocks -> uiState = uiState.copy(contentBlocks = blocks) },
        onSubmitClick = { _ -> } // 필수 파라미터 추가
    )
}