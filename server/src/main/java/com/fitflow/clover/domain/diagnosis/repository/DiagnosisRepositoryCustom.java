package com.fitflow.clover.domain.diagnosis.repository;

import com.fitflow.clover.domain.diagnosis.entity.Diagnosis;

import java.util.Optional;

public interface DiagnosisRepositoryCustom {
    Optional<Diagnosis> findByMemberId(Long memberId);
}
