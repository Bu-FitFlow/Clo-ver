package com.fitflow.clover.domain.diagnosis.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DiagnosisColorRequest {
    private Long memberId;
    private MultipartFile frontImg;
}
