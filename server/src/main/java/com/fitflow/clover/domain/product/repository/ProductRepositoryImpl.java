package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.product.entity.Product;

import static com.fitflow.clover.domain.product.entity.QProduct.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Product> searchProducts(Long cursorId, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        ltProductId(cursorId)
                )
                .orderBy(product.productId.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (products.size() > pageable.getPageSize()) {
            hasNext = true;
            products.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(products, pageable, hasNext);
    }

    private BooleanExpression ltProductId(Long cursorId) {
        // cursorId가 안 들어왔다? (첫 페이지) -> 조건 없이 전체 조회 (return null)
        if (cursorId == null) {
            return null;
        }
        // cursorId가 들어왔다? (스크롤) -> product.productId < cursorId 조건 추가!
        return product.productId.lt(cursorId);
    }
}
