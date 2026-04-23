package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordChangeRequest {
    @NotBlank(message = "현재 비밀번호를 입력해 주세요.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    private String newPassword;
}
