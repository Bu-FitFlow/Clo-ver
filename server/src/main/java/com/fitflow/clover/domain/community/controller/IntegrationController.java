package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.CommentReportRequest;
import com.fitflow.clover.domain.community.dto.request.CommunityReportRequest;
import com.fitflow.clover.domain.community.dto.response.CommentReportResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityRecommendResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityReportResponse;
import com.fitflow.clover.domain.community.service.CommunityRecommendService;
import com.fitflow.clover.domain.community.service.FreeCommunityService; // ★ 실제 존재하는 서비스 주입
import com.fitflow.clover.domain.community.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "기타 기능", description = "메인 인기, 신고")
@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final CommunityRecommendService communityRecommendService;
    private final FreeCommunityService freeCommunityService;
    private final ReportService reportService;

    @Operation(summary = "메인 화면 인기 게시글")
    @GetMapping("/popular")
    public ResponseEntity<List<CommunityRecommendResponse>> getPopularPosts(
            @RequestParam String boardType
    ) {
        List<CommunityRecommendResponse> responses = communityRecommendService.getPopularPosts(boardType);
        return ResponseEntity.ok(responses);
    }

    @Tag(name = "신고 관리", description = "신고 관련 API")
    @Operation(summary = "게시글 신고")
    @PostMapping("/report/community")
    public ResponseEntity<CommunityReportResponse> reportCommunity(
            @RequestBody @Valid CommunityReportRequest request
    ) {
        CommunityReportResponse response = reportService.processCommunityReport(request, 1L);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "악성 댓글 신고")
    @PostMapping("/report/comment")
    public ResponseEntity<CommentReportResponse> reportComment(
            @RequestBody @Valid CommentReportRequest request
    ) {
        CommentReportResponse response = reportService.processCommentReport(request, 1L);
        return ResponseEntity.ok(response);
    }
}