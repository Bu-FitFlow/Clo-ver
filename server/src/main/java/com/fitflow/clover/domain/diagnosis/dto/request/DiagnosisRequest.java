package com.fitflow.clover.domain.diagnosis.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DiagnosisRequest {
    private Long memberId;
    private String gender;
    private Integer height;
    private Integer weight;
    private MultipartFile frontImg;
    private MultipartFile sideImg;
}