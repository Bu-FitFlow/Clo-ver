package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.CommentReportRequest;
import com.fitflow.clover.domain.community.dto.request.CommunityReportRequest;
import com.fitflow.clover.domain.community.dto.response.CommentReportResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityRecommendResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityReportResponse;
import com.fitflow.clover.domain.community.service.CommunityRecommendService;
import com.fitflow.clover.domain.community.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기타 기능", description = "메인 인기, 신고")
@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final CommunityRecommendService communityRecommendService;
    private final ReportService reportService;

    @Operation(summary = "메인 화면 인기 게시글 (게시판별 5개씩)")
    @GetMapping("/popular")
    public ResponseEntity<List<CommunityRecommendResponse>> getPopularPosts() {
        return ResponseEntity.ok(communityRecommendService.getPopularPosts());
    }

    @Operation(summary = "게시글 신고")
    @PostMapping("/report/community")
    public ResponseEntity<CommunityReportResponse> reportCommunity(
            @RequestBody @Valid CommunityReportRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(reportService.processCommunityReport(request, memberId));
    }

    @Operation(summary = "악성 댓글 신고")
    @PostMapping("/report/comment")
    public ResponseEntity<CommentReportResponse> reportComment(
            @RequestBody @Valid CommentReportRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(reportService.processCommentReport(request, memberId));
    }
}