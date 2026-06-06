package com.fitflow.clover.domain.usecase

import com.fitflow.clover.domain.modal.CommunityCategory
import com.fitflow.clover.domain.modal.CommunityPost
import com.fitflow.clover.domain.modal.CommunityPostSummary
import com.fitflow.clover.domain.repository.CommunityRepository

class CommunityUseCase(
    private val communityRepository: CommunityRepository
) {

    suspend fun getPosts(category: CommunityCategory): List<CommunityPostSummary> {
        return communityRepository.getPosts(category)
    }

    suspend fun getPostDetail(communityId: Long, category: CommunityCategory): CommunityPost {
        return communityRepository.getPostDetail(communityId, category)
    }

    suspend fun createPost(
        category: CommunityCategory,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost {
        return communityRepository.createPost(category, title, content, imageUrl)
    }

    suspend fun updatePost(
        communityId: Long,
        category: CommunityCategory,
        title: String,
        content: String,
        imageUrl: String? = null
    ): CommunityPost {
        return communityRepository.updatePost(communityId, category, title, content, imageUrl)
    }

    suspend fun deletePost(communityId: Long, category: CommunityCategory) {
        communityRepository.deletePost(communityId, category)
    }

    suspend fun likePost(communityId: Long, category: CommunityCategory) {
        communityRepository.likePost(communityId, category)
    }

    suspend fun createComment(communityId: Long, content: String) {
        communityRepository.createComment(communityId, content)
    }

    suspend fun updateComment(communityId: Long, commentId: Long, content: String) {
        communityRepository.updateComment(communityId, commentId, content)
    }

    suspend fun deleteComment(communityId: Long, commentId: Long) {
        communityRepository.deleteComment(communityId, commentId)
    }

    suspend fun createReply(communityId: Long, parentId: Long, content: String) {
        communityRepository.createReply(communityId, parentId, content)
    }

    suspend fun deleteReply(communityId: Long, parentId: Long, commentId: Long) {
        communityRepository.deleteReply(communityId, parentId, commentId)
    }

    suspend fun blockUser(nickname: String) {
        communityRepository.blockUser(nickname)
    }

    suspend fun unblockUser(nickname: String) {
        communityRepository.unblockUser(nickname)
    }
}