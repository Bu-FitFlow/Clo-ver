package com.fitflow.clover.domain.repository

import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary

interface CommunityRepository {

    suspend fun getPosts(
        category: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<CommunityPostSummary>

    suspend fun getPostDetail(postId: Long): CommunityPost

    suspend fun createPost(
        category: String,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost

    suspend fun updatePost(
        postId: Long,
        category: String,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost

    suspend fun deletePost(postId: Long)

    suspend fun likePost(postId: Long)

    suspend fun unlikePost(postId: Long)

    suspend fun createComment(postId: Long, content: String)

    suspend fun updateComment(postId: Long, commentId: Long, content: String)

    suspend fun deleteComment(postId: Long, commentId: Long)

    suspend fun createReply(postId: Long, commentId: Long, content: String)

    suspend fun updateReply(postId: Long, commentId: Long, replyId: Long, content: String)

    suspend fun deleteReply(postId: Long, commentId: Long, replyId: Long)

    suspend fun reportPost(postId: Long, reason: String)

    suspend fun blockUser(userId: Long)
}