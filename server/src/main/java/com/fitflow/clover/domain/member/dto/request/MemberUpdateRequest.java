package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        String nickname,

        @NotBlank(message = "이메은 필수 입력 값입니다.")
        String email
) {
}