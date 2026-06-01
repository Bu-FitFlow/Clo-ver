package com.fitflow.clover.domain.review.repository;

import com.fitflow.clover.domain.review.entity.DealReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fitflow.clover.domain.review.entity.QDealReview.dealReview;

@Repository
@RequiredArgsConstructor
public class DealReviewRepositoryImpl implements DealReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<DealReview> findReceivedReviews(Long targetId, Pageable pageable) {
        List<DealReview> content = queryFactory
                .selectFrom(dealReview)
                .join(dealReview.deal).fetchJoin()
                .join(dealReview.deal.product).fetchJoin()
                .join(dealReview.author).fetchJoin()
                .where(dealReview.target.memberId.eq(targetId))
                .orderBy(dealReview.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, content);
    }

    @Override
    public Slice<DealReview> findWrittenReviews(Long authorId, Pageable pageable) {
        List<DealReview> content = queryFactory
                .selectFrom(dealReview)
                .join(dealReview.deal).fetchJoin()
                .join(dealReview.deal.product).fetchJoin()
                .join(dealReview.target).fetchJoin()
                .where(dealReview.author.memberId.eq(authorId))
                .orderBy(dealReview.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, content);
    }

    private Slice<DealReview> checkLastPage(Pageable pageable, List<DealReview> content) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
