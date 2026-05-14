package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetSendRequest(
        @NotBlank
        String loginId,

        @NotBlank
        String name,

        @NotBlank
        String email
) {
}