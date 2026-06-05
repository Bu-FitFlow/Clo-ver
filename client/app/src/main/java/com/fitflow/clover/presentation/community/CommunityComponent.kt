package com.fitflow.clover.presentation.community

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityComment
import com.fitflow.clover.domain.modal.CommunityContentBlock
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.modal.CommunityReply

private val CloverGreen = Color(0xFF99DE81)
private val CloverGreenLight = Color(0xFFE8F8E0)

// ─────────────────────────────────────────────────────────
// 1. 게시글 목록 아이템
// ─────────────────────────────────────────────────────────
@Composable
fun PostItem(
    post: CommunityPostSummary,
    onPostClick: (Long) -> Unit,
    // postId를 전달, Screen에서 isMyPost 판단
    onMenuClick: (Long) -> Unit,
    isMenuExpanded: Boolean = false,
    isMyPost: Boolean = false,
    onMenuDismiss: () -> Unit = {},
    onEditClick: (Long) -> Unit = {},
    onDeleteClick: (Long) -> Unit = {},
    onReportClick: (Long) -> Unit = {},
    onBlockClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onPostClick(post.postId) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "[${post.category.displayName}]",
                    fontSize = 10.sp,
                    color = CloverGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = post.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = post.contentPreview,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(post.createdAt, fontSize = 10.sp, color = Color.LightGray)
                    Text("💬 ${post.commentCount}", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Box {
                IconButton(
                    onClick = { onMenuClick(post.postId) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기",
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (isMenuExpanded) {
                    ListPostMenuPopup(
                        isMyPost = isMyPost,
                        onEditClick = { onEditClick(post.postId) },
                        onDeleteClick = { onDeleteClick(post.postId) },
                        onReportClick = { onReportClick(post.postId) },
                        onBlockClick = { onBlockClick(post.postId) },
                        onDismiss = onMenuDismiss
                    )
                }
            }
        }

        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

// ─────────────────────────────────────────────────────────
// 2. 카테고리 칩
// ─────────────────────────────────────────────────────────
@Composable
fun CategoryChip(
    category: CommunityCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .width(70.dp)
            .height(35.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) CloverGreen else Color.Black
        ),
        color = if (isSelected) CloverGreen else Color.White
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = category.displayName,
                fontSize = 13.sp,
                color = Color.Black
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 3. 본문 블록 렌더러
// ─────────────────────────────────────────────────────────
@Composable
fun ContentBlockItem(
    block: CommunityContentBlock,
    modifier: Modifier = Modifier
) {
    when (block) {
        is CommunityContentBlock.TextBlock -> {
            Text(
                text = block.text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                lineHeight = 18.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        is CommunityContentBlock.ImageBlock -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                AsyncImage(
                    model = block.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = block.description ?: "-설명 없을 때-",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 4. 댓글 아이템
// ─────────────────────────────────────────────────────────
@Composable
fun CommentItem(
    comment: CommunityComment,
    onReplyClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onEditClick: (Long, String) -> Unit,
    // 수정 모드 관련
    isEditing: Boolean = false,
    editingInput: String = "",
    onEditInputChange: (String) -> Unit = {},
    onEditSubmit: () -> Unit = {},
    onEditCancel: () -> Unit = {},
    // 대댓글 수정/삭제 콜백
    onReplyDeleteClick: (Long, Long) -> Unit = { _, _ -> },
    onReplyEditClick: (Long, String) -> Unit = { _, _ -> },
    isEditingReplyId: Long? = null,
    editingReplyInput: String = "",
    onReplyEditInputChange: (String) -> Unit = {},
    onReplyEditSubmit: (Long) -> Unit = {},
    onReplyEditCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${comment.authorNickname} 님",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = comment.createdAt,
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { onReplyClick(comment.commentId) }) {
                    Text("답글", fontSize = 11.sp, color = Color.Gray)
                }
                if (comment.isMyComment) {
                    TextButton(onClick = { onEditClick(comment.commentId, comment.content) }) {
                        Text("수정", fontSize = 11.sp, color = Color.Gray)
                    }
                    TextButton(onClick = { onDeleteClick(comment.commentId) }) {
                        Text("삭제", fontSize = 11.sp, color = Color.Red)
                    }
                }
            }
        }

        if (isEditing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = editingInput,
                    onValueChange = onEditInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("댓글 수정...", fontSize = 11.sp) },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = CloverGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onEditSubmit) {
                    Text("완료", color = CloverGreen, fontSize = 11.sp)
                }
                TextButton(onClick = onEditCancel) {
                    Text("취소", color = Color.Gray, fontSize = 11.sp)
                }
            }
        } else {
            Text(
                text = comment.content,
                fontSize = 11.sp,
                color = Color.DarkGray,
                lineHeight = 17.sp
            )
        }

        comment.replies.forEach { reply ->
            ReplyItem(
                reply = reply,
                commentId = comment.commentId,
                onDeleteClick = onReplyDeleteClick,
                onEditClick = onReplyEditClick,
                isEditing = isEditingReplyId == reply.replyId,
                editingInput = if (isEditingReplyId == reply.replyId) editingReplyInput else "",
                onEditInputChange = onReplyEditInputChange,
                onEditSubmit = { onReplyEditSubmit(comment.commentId) },
                onEditCancel = onReplyEditCancel,
                modifier = Modifier.padding(start = 16.dp, top = 6.dp)
            )
        }

        HorizontalDivider(
            color = Color.LightGray,
            thickness = 0.5.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────
// 5. 대댓글 아이템
// ─────────────────────────────────────────────────────────
@Composable
fun ReplyItem(
    reply: CommunityReply,
    commentId: Long,
    onDeleteClick: (Long, Long) -> Unit,
    onEditClick: (Long, String) -> Unit,
    isEditing: Boolean = false,
    editingInput: String = "",
    onEditInputChange: (String) -> Unit = {},
    onEditSubmit: () -> Unit = {},
    onEditCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "ㄴ",
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.padding(end = 6.dp, top = 2.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${reply.authorNickname} 님",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Text(
                        text = reply.createdAt,
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
                if (reply.isMyReply) {
                    Row {
                        TextButton(onClick = { onEditClick(reply.replyId, reply.content) }) {
                            Text("수정", fontSize = 10.sp, color = Color.Gray)
                        }
                        TextButton(onClick = { onDeleteClick(commentId, reply.replyId) }) {
                            Text("삭제", fontSize = 10.sp, color = Color.Red)
                        }
                    }
                }
            }

            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = editingInput,
                        onValueChange = onEditInputChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("대댓글 수정...", fontSize = 11.sp) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = CloverGreen,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                    TextButton(onClick = onEditSubmit) {
                        Text("완료", color = CloverGreen, fontSize = 10.sp)
                    }
                    TextButton(onClick = onEditCancel) {
                        Text("취소", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            } else {
                Text(
                    text = reply.content,
                    fontSize = 11.sp,
                    color = Color.DarkGray,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 6. 좋아요 버튼
// ─────────────────────────────────────────────────────────
@Composable
fun LikeButton(
    likeCount: Int,
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isLiked) CloverGreen else Color.LightGray),
        color = if (isLiked) CloverGreenLight else Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isLiked) "❤" else "♡",
                fontSize = 14.sp,
                color = if (isLiked) CloverGreen else Color.Gray
            )
            Text(
                text = "$likeCount",
                fontSize = 13.sp,
                color = if (isLiked) CloverGreen else Color.Gray
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 7. 게시글 더보기 팝업 메뉴
// ─────────────────────────────────────────────────────────
@Composable
fun PostMenuPopup(
    isMyPost: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        if (isMyPost) {
            // 내 글일 때: 수정 / 삭제
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("수정하기", fontSize = 14.sp) },
                onClick = {
                    onEditClick()
                    onDismiss()
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("삭제하기", fontSize = 14.sp, color = Color.Red) },
                onClick = {
                    onDeleteClick()
                    onDismiss()
                }
            )
        } else {
            // 다른 사람 글일 때: 신고 / 차단
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("신고 하기", fontSize = 14.sp) },
                onClick = {
                    onReportClick()
                    onDismiss()
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PersonOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("차단 하기", fontSize = 14.sp) },
                onClick = {
                    onBlockClick()
                    onDismiss()
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 7-1. 목록 화면 게시글 ... 팝업 메뉴 (신고/차단 or 수정/삭제)
// ─────────────────────────────────────────────────────────
@Composable
fun ListPostMenuPopup(
    isMyPost: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        if (isMyPost) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("수정하기", fontSize = 14.sp) },
                onClick = { onEditClick(); onDismiss() }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("삭제하기", fontSize = 14.sp, color = Color.Red) },
                onClick = { onDeleteClick(); onDismiss() }
            )
        } else {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Block,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("신고하기", fontSize = 14.sp) },
                onClick = { onReportClick(); onDismiss() }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PersonOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                },
                text = { Text("차단하기", fontSize = 14.sp) },
                onClick = { onBlockClick(); onDismiss() }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
// 8. 카테고리 드롭다운
// ─────────────────────────────────────────────────────────
@Composable
fun CategoryDropdown(
    selectedCategory: CommunityCategory?,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onCategorySelect: (CommunityCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        CommunityCategory.FREE,
        CommunityCategory.REVIEW,
        CommunityCategory.COORDINATION
    )

    Box(modifier = modifier) {
        Surface(
            onClick = { onExpandChange(!isExpanded) },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.Black),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = selectedCategory?.displayName ?: "커뮤니티 유형 선택",
                    fontSize = 14.sp,
                    color = if (selectedCategory != null) Color.Black else Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Black
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandChange(false) },
            containerColor = Color.White
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.displayName,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onCategorySelect(category)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}