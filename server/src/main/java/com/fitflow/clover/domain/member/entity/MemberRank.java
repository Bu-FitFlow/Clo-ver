package com.fitflow.clover.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRank {
    SEED("씨앗"),
    SPROUT("새싹"),
    THREE_LEAF("세잎클로버"),
    FOUR_LEAF("네잎클로버"),
    GOLDEN("황금클로버");

    private final String description;
}
