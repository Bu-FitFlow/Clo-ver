package com.fitflow.clover.domain.deal.repository;

import com.fitflow.clover.domain.deal.entity.Deal;
import com.fitflow.clover.domain.deal.entity.DealRole;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fitflow.clover.domain.deal.entity.QDeal.deal;

@Repository
@RequiredArgsConstructor
public class DealRepositoryImpl implements DealRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Deal> findMyDeals(Long memberId, DealRole role, Pageable pageable) {
        List<Deal> deals = queryFactory
                .selectFrom(deal)
                .join(deal.product).fetchJoin()
                .join(deal.buyer).fetchJoin()
                .where(roleEq(memberId, role))
                .orderBy(deal.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (deals.size() > pageable.getPageSize()) {
            deals.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(deals, pageable, hasNext);
    }

    private BooleanExpression roleEq(Long memberId, DealRole role) {
        if (role == DealRole.BUYER) return deal.buyer.memberId.eq(memberId);
        else if (role == DealRole.SELLER) return deal.seller.memberId.eq(memberId);

        return null;
    }
}
