package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.request.CommentReportRequest;
import com.fitflow.clover.domain.community.dto.request.CommunityReportRequest;
import com.fitflow.clover.domain.community.dto.response.CommentReportResponse;
import com.fitflow.clover.domain.community.dto.response.CommunityReportResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ReportService {

    public CommunityReportResponse processCommunityReport(CommunityReportRequest request, Long memberId) {
        return new CommunityReportResponse(
                701L,
                request.communityId(),
                request.reportReason(),
                "RECEIVED",
                LocalDateTime.now()
        );
    }


    public CommentReportResponse processCommentReport(CommentReportRequest request, Long memberId) {
        return new CommentReportResponse(
                703L,
                request.commentId(),
                "RECEIVED",
                LocalDateTime.now()
        );
    }
}