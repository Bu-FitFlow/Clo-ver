package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.product.dto.request.ProductSearchCondition;
import com.fitflow.clover.domain.product.entity.Product;

import static com.fitflow.clover.domain.product.entity.QProduct.product;

import com.fitflow.clover.domain.product.entity.ProductStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Product> searchProducts(ProductSearchCondition condition, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        ltProductId(condition.cursorId()),
                        eqCategoryId(condition.categoryId()),
                        eqTradingArea(condition.tradingArea()),
                        containsKeyword(condition.keyword()),
                        isActiveOrEqStatus(condition.status())
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

    @Override
    public Slice<Product> findRecommendedProducts(String recommendedType, String personalColor, ProductStatus status, Pageable pageable) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        matchRecommendedTypeOrPersonalColor(recommendedType, personalColor),
                        product.postStatus.eq(status)
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
        if (cursorId == null) {
            return null;
        }
        return product.productId.lt(cursorId);
    }

    private BooleanExpression eqCategoryId(Long categoryId) {
        return categoryId != null ? product.category.categoryId.eq(categoryId) : null;
    }

    private BooleanExpression eqTradingArea(String tradingArea) {
        return StringUtils.hasText(tradingArea) ? product.tradingArea.eq(tradingArea) : null;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? product.name.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression isActiveOrEqStatus(ProductStatus status) {
        return status != null ? product.postStatus.eq(status) : product.postStatus.eq(ProductStatus.ACTIVE);
    }

    private BooleanExpression matchRecommendedTypeOrPersonalColor(String recommendedType, String personalColor) {
        BooleanExpression condition = null;

        if (StringUtils.hasText(recommendedType) && !recommendedType.equals("TBD")) {
            condition = product.recommendedType.eq(recommendedType);
        }

        if (StringUtils.hasText(personalColor) && !personalColor.equals("TBD")) {
            BooleanExpression colorCondition = product.personalColor.eq(personalColor);
            condition = (condition != null) ? condition.or(colorCondition) : colorCondition;
        }

        return condition;
    }
}
