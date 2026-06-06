package com.fitflow.clover.data.repository

import com.fitflow.clover.data.remote.api.CommunityApi
import com.fitflow.clover.data.remote.dto.CreateCommentRequest
import com.fitflow.clover.data.remote.dto.CreateCommunityPostRequest
import com.fitflow.clover.data.remote.dto.CreateReplyRequest
import com.fitflow.clover.data.remote.dto.UpdateCommunityPostRequest
import com.fitflow.clover.data.remote.dto.toDomain
import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.repository.CommunityRepository

class CommunityRepositoryImpl(
    private val communityApi: CommunityApi
) : CommunityRepository {

    // ─── 게시글 목록 ──────────────────────────────────────────

    override suspend fun getPosts(category: CommunityCategory): List<CommunityPostSummary> {
        return when (category) {
            CommunityCategory.FREE        -> communityApi.getFreePosts()
            CommunityCategory.REVIEW      -> communityApi.getReviewPosts()
            CommunityCategory.COORDINATION -> communityApi.getStylingPosts()
            CommunityCategory.ALL         -> {
                // 전체 탭: 세 게시판 합쳐서 createdAt 기준 정렬
                val free     = communityApi.getFreePosts()
                val review   = communityApi.getReviewPosts()
                val styling  = communityApi.getStylingPosts()
                (free + review + styling).sortedByDescending { it.createdAt }
            }
        }.map { it.toDomain() }
    }

    // ─── 게시글 상세 ──────────────────────────────────────────

    override suspend fun getPostDetail(
        communityId: Long,
        category: CommunityCategory
    ): CommunityPost {
        return when (category) {
            CommunityCategory.FREE, CommunityCategory.ALL ->
                communityApi.getFreePostDetail(communityId)
            CommunityCategory.REVIEW ->
                communityApi.getReviewPostDetail(communityId)
            CommunityCategory.COORDINATION ->
                communityApi.getStylingPostDetail(communityId)
        }.toDomain()
    }

    // ─── 게시글 작성 ──────────────────────────────────────────

    override suspend fun createPost(
        category: CommunityCategory,
        title: String,
        content: String,
        imageUrl: String?
    ): CommunityPost {
        val body = CreateCommunityPostRequest(
            category = category.name,
            title = title,
            content = content,
            imageUrl = imageUrl
        )
        return when (category) {
            CommunityCategory.FREE, CommunityCategory.ALL ->
                communityApi.createFreePost(body)
            CommunityCategory.REVIEW ->
                communityApi.createReviewPost(body)
            CommunityCategory.COORDINATION ->
                communityApi.createStylingPost(body)
        }.toDomain()
    }

    // ─── 게시글 수정 ──────────────────────────────────────────

    override suspend fun updatePost(
        communityId: Long,
        category: CommunityCategory,
        title: String,
        content: String,
        imageUrl: String?
    ): CommunityPost {
        val body = UpdateCommunityPostRequest(
            category = category.name,
            title = title,
            content = content,
            imageUrl = imageUrl
        )
        return when (category) {
            CommunityCategory.FREE, CommunityCategory.ALL ->
                communityApi.updateFreePost(communityId, body)
            CommunityCategory.REVIEW ->
                communityApi.updateReviewPost(communityId, body)
            CommunityCategory.COORDINATION ->
                communityApi.updateStylingPost(communityId, body)
        }.toDomain()
    }

    // ─── 게시글 삭제 ──────────────────────────────────────────

    override suspend fun deletePost(communityId: Long, category: CommunityCategory) {
        when (category) {
            CommunityCategory.FREE, CommunityCategory.ALL ->
                communityApi.deleteFreePost(communityId)
            CommunityCategory.REVIEW ->
                communityApi.deleteReviewPost(communityId)
            CommunityCategory.COORDINATION ->
                communityApi.deleteStylingPost(communityId)
        }
    }

    // ─── 좋아요 ───────────────────────────────────────────────

    override suspend fun likePost(communityId: Long, category: CommunityCategory) {
        when (category) {
            CommunityCategory.FREE, CommunityCategory.ALL ->
                communityApi.likeFreePost(communityId)
            CommunityCategory.REVIEW ->
                communityApi.likeReviewPost(communityId)
            CommunityCategory.COORDINATION ->
                communityApi.likeStylingPost(communityId)
        }
    }

    // ─── 댓글 ────────────────────────────────────────────────

    override suspend fun createComment(communityId: Long, content: String) {
        communityApi.createComment(
            communityId = communityId,
            body = CreateCommentRequest(content = content)
        )
    }

    override suspend fun updateComment(
        communityId: Long,
        commentId: Long,
        content: String
    ) {
        communityApi.updateComment(
            communityId = communityId,
            commentId = commentId,
            body = CreateCommentRequest(content = content)
        )
    }

    override suspend fun deleteComment(communityId: Long, commentId: Long) {
        communityApi.deleteComment(
            communityId = communityId,
            commentId = commentId
        )
    }

    // ─── 대댓글 ───────────────────────────────────────────────

    override suspend fun createReply(
        communityId: Long,
        parentId: Long,
        content: String
    ) {
        communityApi.createReply(
            communityId = communityId,
            parentId = parentId,
            body = CreateReplyRequest(content = content)
        )
    }

    override suspend fun deleteReply(
        communityId: Long,
        parentId: Long,
        commentId: Long
    ) {
        communityApi.deleteReply(
            communityId = communityId,
            parentId = parentId,
            commentId = commentId
        )
    }

    // ─── 차단 ────────────────────────────────────────────────

    override suspend fun blockUser(nickname: String) {
        communityApi.blockUser(nickname)
    }

    override suspend fun unblockUser(nickname: String) {
        communityApi.unblockUser(nickname)
    }
}