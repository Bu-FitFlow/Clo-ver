package com.fitflow.clover.domain.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductCreateRequest(
        @NotNull(message = "카테고리를 선택해 주세요.")
        Long categoryId,

        @NotBlank(message = "상품명을 입력해 주세요.")
        @Size(max = 100, message = "상품명은 100자 이내여야 합니다.")
        String name,

        @NotNull(message = "가격을 입력해 주세요.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        Integer price,

        @NotBlank(message = "상품 설명을 입력해 주세요.")
        String content,

        @NotBlank(message = "사이즈를 입력해 주세요.")
        String size,

        @NotBlank(message = "상품 상태(급)를 입력해 주세요.")
        String grade,

        @NotBlank(message = "거래 지역을 입력해 주세요.")
        String tradingArea,

        String recommendedType,

        String personalColor,

        List<String> hashtags,

        List<MultipartFile> images
) {
}
