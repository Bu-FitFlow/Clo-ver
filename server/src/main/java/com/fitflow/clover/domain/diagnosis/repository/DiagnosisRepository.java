package com.fitflow.clover.domain.diagnosis.repository;

import com.fitflow.clover.domain.diagnosis.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long>, DiagnosisRepositoryCustom {

    Optional<Diagnosis> findByMemberId(Long memberId);

}