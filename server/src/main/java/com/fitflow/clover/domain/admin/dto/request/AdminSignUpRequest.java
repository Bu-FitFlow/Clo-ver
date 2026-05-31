package com.fitflow.clover.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdminSignUpRequest(
        @NotBlank
        String loginId,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 영문, 숫자를 포함하여 8자 이상이어야 합니다.")
        String password,

        @NotBlank
        String name,

        @Email
        String email
) {
}