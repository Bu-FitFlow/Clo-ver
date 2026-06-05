package com.fitflow.clover.domain.repository

import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary

interface CommunityRepository {

    // 카테고리별 목록 조회
    suspend fun getPosts(category: CommunityCategory): List<CommunityPostSummary>

    // 카테고리별 상세 조회
    suspend fun getPostDetail(communityId: Long, category: CommunityCategory): CommunityPost

    // 카테고리별 작성
    suspend fun createPost(
        category: CommunityCategory,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost

    // 카테고리별 수정
    suspend fun updatePost(
        communityId: Long,
        category: CommunityCategory,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost

    // 카테고리별 삭제
    suspend fun deletePost(communityId: Long, category: CommunityCategory)

    // 카테고리별 좋아요
    suspend fun likePost(communityId: Long, category: CommunityCategory)

    // 댓글
    suspend fun createComment(communityId: Long, content: String)
    suspend fun updateComment(communityId: Long, commentId: Long, content: String)
    suspend fun deleteComment(communityId: Long, commentId: Long)

    // 대댓글
    suspend fun createReply(communityId: Long, parentId: Long, content: String)
    suspend fun deleteReply(communityId: Long, parentId: Long, commentId: Long)

    // 차단
    suspend fun blockUser(nickname: String)
    suspend fun unblockUser(nickname: String)
}