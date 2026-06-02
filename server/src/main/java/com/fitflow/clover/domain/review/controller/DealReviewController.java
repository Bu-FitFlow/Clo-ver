package com.fitflow.clover.domain.review.controller;

import com.fitflow.clover.domain.review.dto.request.DealReviewCreateRequest;
import com.fitflow.clover.domain.review.dto.request.DealReviewUpdateRequest;
import com.fitflow.clover.domain.review.dto.response.DealReviewResponse;
import com.fitflow.clover.domain.review.service.DealReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래 후기", description = "거래 후기(Deal Review) 작성 및 조회 관련 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class DealReviewController {

    private final DealReviewService dealReviewService;

    @Operation(summary = "거래 후기 작성", description = "완료된 거래(COMPLETED)에 대해 평점과 후기를 남깁니다. (1 거래당 1회 작성 제한)")
    @PostMapping
    public ResponseEntity<Long> createReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid DealReviewCreateRequest request) {

        Long authorId = Long.parseLong(userDetails.getUsername());

        Long reviewId = dealReviewService.createReview(authorId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

    @Operation(summary = "내가 받은 후기 목록 조회", description = "다른 사용자가 나에게 남긴 후기들을 최신순으로 조회합니다.")
    @GetMapping("/received")
    public ResponseEntity<Slice<DealReviewResponse>> getReceivedReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(dealReviewService.getReceivedReviews(memberId, pageable));
    }

    @Operation(summary = "내가 쓴 후기 목록 조회", description = "내가 다른 사용자에게 남긴 후기들을 최신순으로 조회합니다.")
    @GetMapping("/written")
    public ResponseEntity<Slice<DealReviewResponse>> getWrittenReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(dealReviewService.getWrittenReviews(memberId, pageable));
    }

    @Operation(summary = "거래 후기 수정", description = "작성한 후기를 1회에 한하여 수정합니다. 원본 데이터는 보존됩니다.")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reviewId,
            @RequestBody @Valid DealReviewUpdateRequest request) {

        Long authorId = Long.parseLong(userDetails.getUsername());
        dealReviewService.updateReview(authorId, reviewId, request);

        return ResponseEntity.ok("후기가 성공적으로 수정되었습니다.");
    }
}