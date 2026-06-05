package com.fitflow.clover.data.repository

import com.fitflow.clover.data.remote.api.CommunityApi
import com.fitflow.clover.data.remote.dto.CreateCommentRequest
import com.fitflow.clover.data.remote.dto.CreateCommunityPostRequest
import com.fitflow.clover.data.remote.dto.CreateReplyRequest
import com.fitflow.clover.data.remote.dto.UpdateCommunityPostRequest
import com.fitflow.clover.data.remote.dto.toDomain
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.repository.CommunityRepository

class CommunityRepositoryImpl(
    private val communityApi: CommunityApi
) : CommunityRepository {

    override suspend fun getPosts(
        category: String?,
        page: Int,
        size: Int
    ): List<CommunityPostSummary> {
        return communityApi.getPosts(
            category = category,
            page = page,
            size = size
        ).map { it.toDomain() }
    }

    override suspend fun getPostDetail(
        postId: Long
    ): CommunityPost {
        return communityApi.getPostDetail(postId).toDomain()
    }

    override suspend fun createPost(
        category: String,
        title: String,
        content: String,
        imageUrl: String?
    ): CommunityPost {
        return communityApi.createPost(
            CreateCommunityPostRequest(
                category = category,
                title = title,
                content = content,
                imageUrl = imageUrl
            )
        ).toDomain()
    }

    override suspend fun updatePost(
        postId: Long,
        category: String,
        title: String,
        content: String,
        imageUrl: String?
    ): CommunityPost {
        return communityApi.updatePost(
            postId = postId,
            body = UpdateCommunityPostRequest(
                category = category,
                title = title,
                content = content,
                imageUrl = imageUrl
            )
        ).toDomain()
    }

    override suspend fun deletePost(postId: Long) {
        communityApi.deletePost(postId)
    }

    override suspend fun likePost(postId: Long) {
        communityApi.likePost(postId)
    }

    override suspend fun unlikePost(postId: Long) {
        communityApi.unlikePost(postId)
    }

    override suspend fun createComment(
        postId: Long,
        content: String
    ) {
        communityApi.createComment(
            postId = postId,
            body = CreateCommentRequest(content = content)
        )
    }

    override suspend fun updateComment(
        postId: Long,
        commentId: Long,
        content: String
    ) {
        communityApi.updateComment(
            postId = postId,
            commentId = commentId,
            body = CreateCommentRequest(content = content)
        )
    }

    override suspend fun deleteComment(
        postId: Long,
        commentId: Long
    ) {
        communityApi.deleteComment(
            postId = postId,
            commentId = commentId
        )
    }

    override suspend fun createReply(
        postId: Long,
        commentId: Long,
        content: String
    ) {
        communityApi.createReply(
            postId = postId,
            commentId = commentId,
            body = CreateReplyRequest(content = content)
        )
    }

    override suspend fun updateReply(
        postId: Long,
        commentId: Long,
        replyId: Long,
        content: String
    ) {
        communityApi.updateReply(
            postId = postId,
            commentId = commentId,
            replyId = replyId,
            body = CreateReplyRequest(content = content)
        )
    }

    override suspend fun deleteReply(
        postId: Long,
        commentId: Long,
        replyId: Long
    ) {
        communityApi.deleteReply(
            postId = postId,
            commentId = commentId,
            replyId = replyId
        )
    }

    override suspend fun reportPost(
        postId: Long,
        reason: String
    ) {
        communityApi.reportPost(
            postId = postId,
            body = mapOf("reason" to reason)
        )
    }

    override suspend fun blockUser(userId: Long) {
        communityApi.blockUser(userId)
    }
}