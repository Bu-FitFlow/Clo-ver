package com.fitflow.clover.domain.product.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ACTIVE("판매중"),
    RESERVED("예약중"),
    SOLD_OUT("판매완료"),
    HIDDEN("숨김"),
    DELETED("삭제됨");

    private final String description;
}
