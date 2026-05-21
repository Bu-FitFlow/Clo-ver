package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.ReviewPostRequest;
import com.fitflow.clover.domain.community.dto.response.CommunityListResponse;
import com.fitflow.clover.domain.community.dto.response.ReviewDetailResponse;
import com.fitflow.clover.domain.community.service.ReviewCommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "리뷰게시판", description = "리뷰게시판 통합 관리 API")
@RestController
@RequestMapping("/api/community/review")
@RequiredArgsConstructor
public class ReviewCommunityController {

    private final ReviewCommunityService reviewCommunityService;

    @Operation(summary = "리뷰 게시판 목록 조회")
    @GetMapping
    public ResponseEntity<List<CommunityListResponse>> getReviewList() {
        return ResponseEntity.ok(reviewCommunityService.getReviewList());
    }

    @Operation(summary = "리뷰 게시글 등록")
    @PostMapping
    public ResponseEntity<Long> createReviewPost(
            @RequestBody @Valid ReviewPostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(reviewCommunityService.createReviewPost(request, memberId));
    }

    @Operation(summary = "리뷰 게시글 상세 조회")
    @GetMapping("/{communityId}")
    public ResponseEntity<ReviewDetailResponse> getReviewPost(
            @PathVariable Long communityId
    ) {
        return ResponseEntity.ok(reviewCommunityService.getReviewPost(communityId));
    }

    @Operation(summary = "리뷰 게시글 수정")
    @PutMapping("/{communityId}")
    public ResponseEntity<Void> updateReviewPost(
            @PathVariable Long communityId,
            @RequestBody @Valid ReviewPostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        reviewCommunityService.updateReviewPost(communityId, request, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 게시글 삭제")
    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteReviewPost(
            @PathVariable Long communityId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        reviewCommunityService.deleteReviewPost(communityId, memberId);
        return ResponseEntity.ok().build();
    }
}