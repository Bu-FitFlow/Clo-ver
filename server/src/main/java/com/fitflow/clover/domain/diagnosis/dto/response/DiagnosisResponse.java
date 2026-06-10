package com.fitflow.clover.domain.diagnosis.dto.response;

import com.fitflow.clover.domain.diagnosis.entity.BodyType;
import com.fitflow.clover.domain.diagnosis.entity.Diagnosis;
import com.fitflow.clover.domain.diagnosis.entity.PersonalColor;
import lombok.Getter;

@Getter
public class DiagnosisResponse {
    private final Long diagnosisId;
    private final Long memberId;
    private final BodyType obesityType;
    private final PersonalColor personalColor;
    private final String resultTitle;

    public DiagnosisResponse(Diagnosis diagnosis) {
        this.diagnosisId = diagnosis.getId();
        this.memberId = diagnosis.getMemberId();
        this.obesityType = diagnosis.getObesityType();
        this.personalColor = diagnosis.getPersonalColor();
        this.resultTitle = diagnosis.getResultTitle();
    }
}