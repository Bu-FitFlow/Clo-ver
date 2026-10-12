package com.fitflow.clover.domain.deal.repository;

import com.fitflow.clover.domain.deal.entity.Deal;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long>, DealRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Deal d WHERE d.id = :dealId")
    Optional<Deal> findByIdForUpdate(@Param("dealId") Long dealId);
}
