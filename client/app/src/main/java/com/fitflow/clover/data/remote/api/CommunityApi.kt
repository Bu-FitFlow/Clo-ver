package com.fitflow.clover.data.remote.api

import com.fitflow.clover.data.remote.dto.CommunityCommentResponse
import com.fitflow.clover.data.remote.dto.CommunityPostDetailResponse
import com.fitflow.clover.data.remote.dto.CommunityPostSummaryResponse
import com.fitflow.clover.data.remote.dto.CommunityReplyResponse
import com.fitflow.clover.data.remote.dto.CreateCommentRequest
import com.fitflow.clover.data.remote.dto.CreateCommunityPostRequest
import com.fitflow.clover.data.remote.dto.CreateReplyRequest
import com.fitflow.clover.data.remote.dto.UpdateCommunityPostRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {

    // ─── 게시글 ───────────────────────────────────────────────

    @GET("api/community/posts")
    suspend fun getPosts(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<CommunityPostSummaryResponse>

    @GET("api/community/posts/{post_id}")
    suspend fun getPostDetail(
        @Path("post_id") postId: Long
    ): CommunityPostDetailResponse

    @POST("api/community/posts")
    suspend fun createPost(
        @Body body: CreateCommunityPostRequest
    ): CommunityPostDetailResponse

    @PUT("api/community/posts/{post_id}")
    suspend fun updatePost(
        @Path("post_id") postId: Long,
        @Body body: UpdateCommunityPostRequest
    ): CommunityPostDetailResponse

    @DELETE("api/community/posts/{post_id}")
    suspend fun deletePost(
        @Path("post_id") postId: Long
    )

    // ─── 좋아요 ───────────────────────────────────────────────

    @POST("api/community/posts/{post_id}/like")
    suspend fun likePost(
        @Path("post_id") postId: Long
    )

    @DELETE("api/community/posts/{post_id}/like")
    suspend fun unlikePost(
        @Path("post_id") postId: Long
    )

    // ─── 댓글 ────────────────────────────────────────────────

    @POST("api/community/posts/{post_id}/comments")
    suspend fun createComment(
        @Path("post_id") postId: Long,
        @Body body: CreateCommentRequest
    ): CommunityCommentResponse

    @PUT("api/community/posts/{post_id}/comments/{comment_id}")
    suspend fun updateComment(
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long,
        @Body body: CreateCommentRequest
    ): CommunityCommentResponse

    @DELETE("api/community/posts/{post_id}/comments/{comment_id}")
    suspend fun deleteComment(
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long
    )

    // ─── 대댓글 ───────────────────────────────────────────────

    @POST("api/community/posts/{post_id}/comments/{comment_id}/replies")
    suspend fun createReply(
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long,
        @Body body: CreateReplyRequest
    ): CommunityReplyResponse

    @PUT("api/community/posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    suspend fun updateReply(
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long,
        @Path("reply_id") replyId: Long,
        @Body body: CreateReplyRequest
    ): CommunityReplyResponse

    @DELETE("api/community/posts/{post_id}/comments/{comment_id}/replies/{reply_id}")
    suspend fun deleteReply(
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long,
        @Path("reply_id") replyId: Long
    )

    // ─── 신고 / 차단 ──────────────────────────────────────────

    @POST("api/community/posts/{post_id}/report")
    suspend fun reportPost(
        @Path("post_id") postId: Long,
        @Body body: Map<String, String>
    )

    @POST("api/users/{user_id}/block")
    suspend fun blockUser(
        @Path("user_id") userId: Long
    )
}