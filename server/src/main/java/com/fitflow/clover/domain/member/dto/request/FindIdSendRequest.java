package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindIdSendRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "이메일을 입력해 주세요.")
        String email
) {
}