package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.StylingPostRequest;
import com.fitflow.clover.domain.community.dto.response.CommunityListResponse;
import com.fitflow.clover.domain.community.dto.response.StylingDetailResponse;
import com.fitflow.clover.domain.community.service.StylingCommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "스타일링 게시판", description = "스타일링 게시판 통합 관리 API")
@RestController
@RequestMapping("/api/community/styling")
@RequiredArgsConstructor
public class StylingCommunityController {

    private final StylingCommunityService stylingCommunityService;

    @Operation(summary = "스타일링 게시판 목록 조회")
    @GetMapping
    public ResponseEntity<List<CommunityListResponse>> getStylingList() {
        return ResponseEntity.ok(stylingCommunityService.getStylingList());
    }

    @Operation(summary = "스타일링 게시글 등록")
    @PostMapping
    public ResponseEntity<Long> createStylingPost(
            @RequestBody @Valid StylingPostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(stylingCommunityService.createStylingPost(request, memberId));
    }

    @Operation(summary = "스타일링 게시글 상세 조회")
    @GetMapping("/{communityId}")
    public ResponseEntity<StylingDetailResponse> getStylingPost(
            @PathVariable Long communityId
    ) {
        return ResponseEntity.ok(stylingCommunityService.getStylingPost(communityId));
    }

    @Operation(summary = "스타일링 게시글 수정")
    @PutMapping("/{communityId}")
    public ResponseEntity<Void> updateStylingPost(
            @PathVariable Long communityId,
            @RequestBody @Valid StylingPostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        stylingCommunityService.updateStylingPost(communityId, request, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스타일링 게시글 삭제")
    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteStylingPost(
            @PathVariable Long communityId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        stylingCommunityService.deleteStylingPost(communityId, memberId);
        return ResponseEntity.ok().build();
    }
}