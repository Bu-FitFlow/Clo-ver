package com.fitflow.clover.domain.usecase

import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.repository.CommunityRepository

class CommunityUseCase(
    private val communityRepository: CommunityRepository
) {

    suspend fun getPosts(
        category: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<CommunityPostSummary> {
        return communityRepository.getPosts(
            category = category,
            page = page,
            size = size
        )
    }

    suspend fun getPostDetail(
        postId: Long
    ): CommunityPost {
        return communityRepository.getPostDetail(
            postId = postId
        )
    }

    suspend fun createPost(
        category: String,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost {
        return communityRepository.createPost(
            category = category,
            title = title,
            content = content,
            imageUrl = imageUrl
        )
    }

    suspend fun updatePost(
        postId: Long,
        category: String,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost {
        return communityRepository.updatePost(
            postId = postId,
            category = category,
            title = title,
            content = content,
            imageUrl = imageUrl
        )
    }

    suspend fun deletePost(
        postId: Long
    ) {
        communityRepository.deletePost(
            postId = postId
        )
    }

    suspend fun likePost(
        postId: Long
    ) {
        communityRepository.likePost(
            postId = postId
        )
    }

    suspend fun unlikePost(
        postId: Long
    ) {
        communityRepository.unlikePost(
            postId = postId
        )
    }

    suspend fun createComment(
        postId: Long,
        content: String
    ) {
        communityRepository.createComment(
            postId = postId,
            content = content
        )
    }

    suspend fun updateComment(
        postId: Long,
        commentId: Long,
        content: String
    ) {
        communityRepository.updateComment(
            postId = postId,
            commentId = commentId,
            content = content
        )
    }

    suspend fun deleteComment(
        postId: Long,
        commentId: Long
    ) {
        communityRepository.deleteComment(
            postId = postId,
            commentId = commentId
        )
    }

    suspend fun createReply(
        postId: Long,
        commentId: Long,
        content: String
    ) {
        communityRepository.createReply(
            postId = postId,
            commentId = commentId,
            content = content
        )
    }

    suspend fun updateReply(
        postId: Long,
        commentId: Long,
        replyId: Long,
        content: String
    ) {
        communityRepository.updateReply(
            postId = postId,
            commentId = commentId,
            replyId = replyId,
            content = content
        )
    }

    suspend fun deleteReply(
        postId: Long,
        commentId: Long,
        replyId: Long
    ) {
        communityRepository.deleteReply(
            postId = postId,
            commentId = commentId,
            replyId = replyId
        )
    }

    suspend fun reportPost(
        postId: Long,
        reason: String
    ) {
        communityRepository.reportPost(
            postId = postId,
            reason = reason
        )
    }

    suspend fun blockUser(
        userId: Long
    ) {
        communityRepository.blockUser(
            userId = userId
        )
    }
}