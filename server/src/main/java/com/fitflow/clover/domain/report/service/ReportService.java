package com.fitflow.clover.domain.report.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.report.dto.ReportCreateRequest;
import com.fitflow.clover.domain.report.dto.ReportProcessRequest;
import com.fitflow.clover.domain.report.dto.ReportResponse;
import com.fitflow.clover.domain.report.entity.Report;
import com.fitflow.clover.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReportResponse createReport(ReportCreateRequest request) {
        Member reporter = memberRepository.findById(request.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("신고자를 찾을 수 없습니다."));

        Member reported = memberRepository.findById(request.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException("피신고자를 찾을 수 없습니다."));

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .communityId(request.getCommunityId())
                .reportType(request.getReportType())
                .content(request.getContent())
                .build();

        Report savedReport = reportRepository.save(report);
        return ReportResponse.from(savedReport);
    }

    public List<ReportResponse> getReports() {
        return reportRepository.findAll()
                .stream()
                .map(ReportResponse::from)
                .toList();
    }

    public ReportResponse getReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        return ReportResponse.from(report);
    }

    @Transactional
    public ReportResponse resolveReport(Long reportId, ReportProcessRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        report.resolve(request.getAdminMemo());
        return ReportResponse.from(report);
    }

    @Transactional
    public ReportResponse rejectReport(Long reportId, ReportProcessRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        report.reject(request.getAdminMemo());
        return ReportResponse.from(report);
    }
}