package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.CommentReportRequest;
import com.fitflow.clover.domain.community.dto.request.CommunityReportRequest;
import com.fitflow.clover.domain.community.dto.request.FreePostRequest;
import com.fitflow.clover.domain.community.dto.response.*;
import com.fitflow.clover.domain.community.service.FreeCommunityService;
import com.fitflow.clover.domain.community.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "자유 게시판", description = "자유 게시판 통합 관리 API")
@RestController
@RequestMapping("/api/community/free")
@RequiredArgsConstructor
public class FreeCommunityController {

    private final FreeCommunityService freeCommunityService;
    private final ReportService reportService;

    @Operation(summary = "자유 게시판 목록 조회")
    @GetMapping
    public ResponseEntity<List<CommunityListResponse>> getFreeList(
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(freeCommunityService.getFreeList(memberId));
    }

    @Operation(summary = "자유 게시판 글 등록")
    @PostMapping
    public ResponseEntity<Long> createFreePost(
            @RequestBody @Valid FreePostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(freeCommunityService.createFreePost(request, memberId));
    }

    @Operation(summary = "자유 게시판 상세 조회")
    @GetMapping("/{communityId}")
    public ResponseEntity<FreeDetailResponse> getFreePost(
            @PathVariable Long communityId
    ) {
        return ResponseEntity.ok(freeCommunityService.getFreePost(communityId));
    }

    @Operation(summary = "자유 게시판 글 수정")
    @PutMapping("/{communityId}")
    public ResponseEntity<Void> updateFreePost(
            @PathVariable Long communityId,
            @RequestBody @Valid FreePostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        freeCommunityService.updateFreePost(communityId, request, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "자유 게시판 글 삭제")
    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteFreePost(
            @PathVariable Long communityId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        freeCommunityService.deleteFreePost(communityId, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "자유 게시판 좋아요")
    @PostMapping("/{communityId}/like")
    public ResponseEntity<Integer> toggleLike(
            @PathVariable Long communityId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(freeCommunityService.toggleLike(communityId, memberId));
    }


}