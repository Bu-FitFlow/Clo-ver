package com.fitflow.clover.domain.diagnosis.controller;

import com.fitflow.clover.domain.diagnosis.dto.request.DiagnosisRequest;
import com.fitflow.clover.domain.diagnosis.dto.response.DiagnosisResponse;
import com.fitflow.clover.domain.diagnosis.entity.Diagnosis;
import com.fitflow.clover.domain.diagnosis.service.DiagnosisService;
import com.fitflow.clover.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "체형 분석", description = "AI 신체 특징 및 체형 진단 관련 API")
@RestController
@RequestMapping("/api/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Operation(summary = "AI 체형 분석 및 진단 결과 생성", description = "사용자의 성별, 신체 정보(키, 몸무게)와 정면/측면 사진을 기반으로 외부 AI 서버와 통신하여 체형을 분석하고 결과를 저장합니다."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DiagnosisResponse> createBodyScan(@ModelAttribute DiagnosisRequest request) {
        Diagnosis diagnosis = diagnosisService.createBodyScan(
                request.getMemberId(),
                request.getGender(),
                request.getHeight(),
                request.getWeight(),
                request.getFrontImg(),
                request.getSideImg()
        );

        return ApiResponse.success(new DiagnosisResponse(diagnosis));
    }
}