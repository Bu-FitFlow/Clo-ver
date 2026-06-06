package com.fitflow.clover.data.remote.api

import com.fitflow.clover.data.remote.dto.CommunityCommentResponse
import com.fitflow.clover.data.remote.dto.CommunityPostDetailResponse
import com.fitflow.clover.data.remote.dto.CommunityPostSummaryResponse
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

interface CommunityApi {

    // ─── 자유 게시판 ──────────────────────────────────────────

    @GET("api/community/free")
    suspend fun getFreePosts(): List<CommunityPostSummaryResponse>

    @GET("api/community/free/{communityId}")
    suspend fun getFreePostDetail(
        @Path("communityId") communityId: Long
    ): CommunityPostDetailResponse

    @POST("api/community/free")
    suspend fun createFreePost(
        @Body body: CreateCommunityPostRequest
    ): CommunityPostDetailResponse

    @PUT("api/community/free/{communityId}")
    suspend fun updateFreePost(
        @Path("communityId") communityId: Long,
        @Body body: UpdateCommunityPostRequest
    ): CommunityPostDetailResponse

    @DELETE("api/community/free/{communityId}")
    suspend fun deleteFreePost(
        @Path("communityId") communityId: Long
    )

    @POST("api/community/free/{communityId}/like")
    suspend fun likeFreePost(
        @Path("communityId") communityId: Long
    )

    // ─── 리뷰 게시판 ──────────────────────────────────────────

    @GET("api/community/review")
    suspend fun getReviewPosts(): List<CommunityPostSummaryResponse>

    @GET("api/community/review/{communityId}")
    suspend fun getReviewPostDetail(
        @Path("communityId") communityId: Long
    ): CommunityPostDetailResponse

    @POST("api/community/review")
    suspend fun createReviewPost(
        @Body body: CreateCommunityPostRequest
    ): CommunityPostDetailResponse

    @PUT("api/community/review/{communityId}")
    suspend fun updateReviewPost(
        @Path("communityId") communityId: Long,
        @Body body: UpdateCommunityPostRequest
    ): CommunityPostDetailResponse

    @DELETE("api/community/review/{communityId}")
    suspend fun deleteReviewPost(
        @Path("communityId") communityId: Long
    )

    @POST("api/community/review/{communityId}/like")
    suspend fun likeReviewPost(
        @Path("communityId") communityId: Long
    )

    // ─── 스타일링 게시판 ──────────────────────────────────────

    @GET("api/community/styling")
    suspend fun getStylingPosts(): List<CommunityPostSummaryResponse>

    @GET("api/community/styling/{communityId}")
    suspend fun getStylingPostDetail(
        @Path("communityId") communityId: Long
    ): CommunityPostDetailResponse

    @POST("api/community/styling")
    suspend fun createStylingPost(
        @Body body: CreateCommunityPostRequest
    ): CommunityPostDetailResponse

    @PUT("api/community/styling/{communityId}")
    suspend fun updateStylingPost(
        @Path("communityId") communityId: Long,
        @Body body: UpdateCommunityPostRequest
    ): CommunityPostDetailResponse

    @DELETE("api/community/styling/{communityId}")
    suspend fun deleteStylingPost(
        @Path("communityId") communityId: Long
    )

    @POST("api/community/styling/{communityId}/like")
    suspend fun likeStylingPost(
        @Path("communityId") communityId: Long
    )

    // ─── 댓글 ────────────────────────────────────────────────

    @GET("api/community/{communityId}/comments")
    suspend fun getComments(
        @Path("communityId") communityId: Long
    ): List<CommunityCommentResponse>

    @POST("api/community/{communityId}/comments")
    suspend fun createComment(
        @Path("communityId") communityId: Long,
        @Body body: CreateCommentRequest
    ): CommunityCommentResponse

    @PUT("api/community/{communityId}/comments/{commentId}")
    suspend fun updateComment(
        @Path("communityId") communityId: Long,
        @Path("commentId") commentId: Long,
        @Body body: CreateCommentRequest
    ): CommunityCommentResponse

    @DELETE("api/community/{communityId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("communityId") communityId: Long,
        @Path("commentId") commentId: Long
    )

    // ─── 대댓글 ───────────────────────────────────────────────

    @POST("api/community/{communityId}/comments/{parentId}/reply")
    suspend fun createReply(
        @Path("communityId") communityId: Long,
        @Path("parentId") parentId: Long,
        @Body body: CreateReplyRequest
    ): CommunityCommentResponse

    @DELETE("api/community/{communityId}/comments/{parentId}/reply/{commentId}")
    suspend fun deleteReply(
        @Path("communityId") communityId: Long,
        @Path("parentId") parentId: Long,
        @Path("commentId") commentId: Long
    )

    // ─── 차단 ────────────────────────────────────────────────

    @POST("api/block/{nickname}")
    suspend fun blockUser(
        @Path("nickname") nickname: String
    )

    @DELETE("api/block/{nickname}")
    suspend fun unblockUser(
        @Path("nickname") nickname: String
    )
}