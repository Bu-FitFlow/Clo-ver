package com.fitflow.clover.domain.deal.repository;

import com.fitflow.clover.domain.deal.entity.DealRole;
import com.fitflow.clover.domain.deal.entity.Deal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DealRepositoryCustom {
    Slice<Deal> findMyDeals(Long memberId, DealRole rold, Pageable pageable);
}
