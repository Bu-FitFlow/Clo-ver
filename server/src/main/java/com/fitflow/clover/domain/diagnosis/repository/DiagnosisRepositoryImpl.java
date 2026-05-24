package com.fitflow.clover.domain.diagnosis.repository;

import com.fitflow.clover.domain.diagnosis.entity.Diagnosis;
import com.fitflow.clover.domain.diagnosis.entity.QDiagnosis;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DiagnosisRepositoryImpl implements DiagnosisRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DiagnosisRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Diagnosis> findByMemberId(Long memberId) {
        QDiagnosis diagnosis = QDiagnosis.diagnosis;

        Diagnosis result = queryFactory
                .selectFrom(diagnosis)
                .where(diagnosis.memberId.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
