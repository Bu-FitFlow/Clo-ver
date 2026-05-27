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

@Tag(name = "신고 관리 API (App/공통)", description = "클라이언트 애플리케이션에서 호출하는 신고 접수 및 데이터 조회/처리용 REST API입니다.")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고 접수", description = "유저가 특정 대상(커뮤니티 글, 회원 등)에 대해 신고를 접수합니다.")
    @PostMapping
    public ResponseEntity<ReportResponse> createReport(@RequestBody ReportCreateRequest request) {
        return ResponseEntity.ok(reportService.createReport(request));
    }

    @Operation(summary = "전체 신고 목록 조회", description = "접수된 모든 신고 내역을 JSON 배열 형태로 반환합니다.")
    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(reportService.getReports());
    }

    @Operation(summary = "단건 신고 상세 조회", description = "신고 ID(reportId)를 기반으로 특정 신고의 상세 내용 및 현재 처리 상태를 반환합니다.")
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @Operation(summary = "신고 처리 완료 (API)", description = "특정 신고 건의 상태를 '승인(RESOLVED)'으로 변경하고 관리자 메모를 기록한 뒤, 갱신된 데이터를 반환합니다.")
    @PatchMapping("/{reportId}/resolve")
    public ResponseEntity<ReportResponse> resolveReport(
            @PathVariable Long reportId,
            @RequestBody ReportProcessRequest request
    ) {
        return ResponseEntity.ok(reportService.resolveReport(reportId, request));
    }

    @Operation(summary = "신고 반려 (API)", description = "특정 신고 건의 상태를 '반려(REJECTED)'로 변경하고 반려 사유(관리자 메모)를 기록한 뒤, 갱신된 데이터를 반환합니다.")
    @PatchMapping("/{reportId}/reject")
    public ResponseEntity<ReportResponse> rejectReport(
            @PathVariable Long reportId,
            @RequestBody ReportProcessRequest request
    ) {
        return ResponseEntity.ok(reportService.rejectReport(reportId, request));
    }
}