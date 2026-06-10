package com.fitflow.clover.domain.diagnosis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BodyType {
    LEAN_COLUMN("The Lean Column"),
    APPLE("The Apple"),
    INVERTED_TRIANGLE("The Inverted Triangle"),
    PEAR("The Pear"),
    HOUR_GLASS("The Hour Glass"),
    RECTANGLE("The Rectangle");

    private final String description;

    @JsonCreator
    public static BodyType fromDescription(String description) {
        for (BodyType type : BodyType.values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}