package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordResetSendRequest {
    @NotBlank
    private String loginId;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}