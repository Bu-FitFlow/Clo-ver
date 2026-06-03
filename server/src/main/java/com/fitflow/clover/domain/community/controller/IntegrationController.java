package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.CommentReportRequest;
import com.fitflow.clover.domain.community.dto.request.CommunityReportRequest;
import com.fitflow.clover.domain.community.dto.response.CommentReportResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityRecommendResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityReportResponse;
import com.fitflow.clover.domain.community.entity.Comment;
import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.repository.CommentRepository;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import com.fitflow.clover.domain.community.service.CommunityRecommendService;
import com.fitflow.clover.domain.report.dto.ReportCreateRequest;
import com.fitflow.clover.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "기타 기능", description = "메인 인기, 신고")
@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final CommunityRecommendService communityRecommendService;
    private final ReportService reportService;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

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
        Long reporterId = Long.parseLong(authentication.getName());

        Community community = communityRepository.findById(request.communityId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        reportService.createReport(new ReportCreateRequest(
                reporterId,
                community.getMemberId(),
                "COMMUNITY",
                request.communityId(),
                request.reportReason(),
                request.reportReason()
        ));

        return ResponseEntity.ok(new CommunityReportResponse(
                request.communityId(),
                request.reportReason(),
                "RECEIVED",
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "악성 댓글 신고")
    @PostMapping("/report/comment")
    public ResponseEntity<CommentReportResponse> reportComment(
            @RequestBody @Valid CommentReportRequest request,
            Authentication authentication
    ) {
        Long reporterId = Long.parseLong(authentication.getName());

        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        reportService.createReport(new ReportCreateRequest(
                reporterId,
                comment.getMemberId(),
                "COMMENT",
                request.commentId(),
                request.reportReason(),
                request.reportReason()
        ));

        return ResponseEntity.ok(new CommentReportResponse(
                request.commentId(),
                request.reportReason(),
                "RECEIVED",
                LocalDateTime.now()
        ));
    }
}