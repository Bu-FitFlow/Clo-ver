package com.fitflow.clover.domain.report.controller;

import com.fitflow.clover.domain.report.dto.ReportCreateRequest;
import com.fitflow.clover.domain.report.dto.ReportProcessRequest;
import com.fitflow.clover.domain.report.dto.ReportResponse;
import com.fitflow.clover.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "신고 관리", description = "신고 생성, 조회, 처리 관련 API")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고 생성")
    @PostMapping
    public ResponseEntity<ReportResponse> createReport(@RequestBody ReportCreateRequest request) {
        return ResponseEntity.ok(reportService.createReport(request));
    }

    @Operation(summary = "신고 목록 조회")
    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(reportService.getReports());
    }

    @Operation(summary = "신고 상세 조회")
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @Operation(summary = "신고 처리 완료")
    @PatchMapping("/{reportId}/resolve")
    public ResponseEntity<ReportResponse> resolveReport(
            @PathVariable Long reportId,
            @RequestBody ReportProcessRequest request
    ) {
        return ResponseEntity.ok(reportService.resolveReport(reportId, request));
    }

    @Operation(summary = "신고 반려")
    @PatchMapping("/{reportId}/reject")
    public ResponseEntity<ReportResponse> rejectReport(
            @PathVariable Long reportId,
            @RequestBody ReportProcessRequest request
    ) {
        return ResponseEntity.ok(reportService.rejectReport(reportId, request));
    }
}