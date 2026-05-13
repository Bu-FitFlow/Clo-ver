package com.fitflow.clover.domain.deal.repository;

import com.fitflow.clover.domain.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long>, DealRepositoryCustom {
}
