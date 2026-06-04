package com.fitflow.clover.domain.report.controller;

import com.fitflow.clover.domain.report.dto.ReportProcessRequest;
import com.fitflow.clover.domain.report.dto.ReportResponse;
import com.fitflow.clover.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 신고 내역 관리 (Web)", description = "관리자 전용 웹 페이지의 신고 내역 목록 조회 및 처리(승인/반려) 폼 요청을 처리하는 웹 컨트롤러입니다.")
@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class AdminReportController {
    private final ReportService reportService;

    @Operation(summary = "신고 내역 목록 페이지 조회", description = "페이징 처리된 전체 신고 내역 목록을 조회하여 타임리프 뷰(admin/report-list.html)에 바인딩하고 반환합니다.")
    @GetMapping
    public String reportList(
            @PageableDefault(size = 10) Pageable pageable,
            Model model
    ) {
        Page<ReportResponse> pagedReports = reportService.getPagedReports(pageable);
        model.addAttribute("reports", pagedReports.getContent());
        model.addAttribute("page", pagedReports);
        return "admin/report-list"; // 타임리프 파일 경로
    }

    @Operation(summary = "신고 승인(완료) 처리", description = "특정 신고 건에 대해 관리자 메모를 남기고 상태를 '승인(RESOLVED)'으로 변경한 뒤, 신고 내역 목록 페이지로 리다이렉트합니다.")
    @PostMapping("/{reportId}/resolve")
    public String resolveReport(
            @PathVariable Long reportId,
            @ModelAttribute ReportProcessRequest request
    ) {
        reportService.resolveReport(reportId, request);
        return "redirect:/reports";
    }

    @Operation(summary = "신고 반려 처리", description = "특정 신고 건에 대해 반려 사유(관리자 메모)를 남기고 상태를 '반려(REJECTED)'로 변경한 뒤, 신고 내역 목록 페이지로 리다이렉트합니다.")
    @PostMapping("/{reportId}/reject")
    public String rejectReport(
            @PathVariable Long reportId,
            @ModelAttribute ReportProcessRequest request
    ) {
        reportService.rejectReport(reportId, request);
        return "redirect:/reports";
    }
}
