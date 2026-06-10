package com.fitflow.clover.domain.diagnosis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "퍼스널 컬러 진단 결과 Enum")
public enum PersonalColor {

    @Schema(description = "봄 웜톤")
    SPRING_WARM("Spring Warm"),

    @Schema(description = "여름 쿨톤")
    SUMMER_COOL("Summer Cool"),

    @Schema(description = "가을 웜톤")
    AUTUMN_WARM("Autumn Warm"),

    @Schema(description = "겨울 쿨톤")
    WINTER_COOL("Winter Cool");

    private final String description;

    @JsonCreator
    public static PersonalColor fromDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return null;
        }
        for (PersonalColor color : PersonalColor.values()) {
            if (color.description.equalsIgnoreCase(description)) {
                return color;
            }
        }
        return null;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}