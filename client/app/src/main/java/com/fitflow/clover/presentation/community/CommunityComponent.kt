package com.fitflow.clover.presentation.community

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
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
    onMenuClick: (Long) -> Unit,
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
                // 카테고리 태그
                Text(
                    text = "[${post.category.displayName}]",
                    fontSize = 12.sp,
                    color = CloverGreen,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 제목
                Text(
                    text = post.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 본문 미리보기
                Text(
                    text = post.contentPreview,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 시간 + 댓글
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(post.createdAt, fontSize = 12.sp, color = Color.LightGray)
                    Text("💬 ${post.commentCount}", fontSize = 12.sp, color = Color.Gray)
                }
            }

            // 더보기 버튼 (⋮)
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
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) CloverGreen else Color.Black
        ),
        color = if (isSelected) CloverGreen else Color.White
    ) {
        Text(
            text = category.displayName,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp),
            fontSize = 14.sp,
            color = Color.Black
        )
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                lineHeight = 22.sp,
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
                    fontSize = 12.sp,
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
            Text(
                text = "${comment.authorNickname} 님",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            TextButton(onClick = { onReplyClick(comment.commentId) }) {
                Text("답글", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Text(
            text = comment.content,
            fontSize = 13.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp
        )

        comment.replies.forEach { reply ->
            ReplyItem(
                reply = reply,
                onDeleteClick = onDeleteClick,
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
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "ㄴ",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(end = 6.dp, top = 2.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "답변: ${reply.authorNickname} 님",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Text(
                text = reply.content,
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
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
            DropdownMenuItem(
                text = { Text("수정하기", fontSize = 14.sp) },
                onClick = {
                    onEditClick()
                    onDismiss()
                }
            )
            DropdownMenuItem(
                text = { Text("삭제하기", fontSize = 14.sp, color = Color.Red) },
                onClick = {
                    onDeleteClick()
                    onDismiss()
                }
            )
        } else {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(
                            id = android.R.drawable.ic_menu_close_clear_cancel
                        ),
                        contentDescription = null,
                        tint = Color.Gray,
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
                        painter = androidx.compose.ui.res.painterResource(
                            id = android.R.drawable.ic_menu_close_clear_cancel
                        ),
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