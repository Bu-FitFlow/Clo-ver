package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordResetRequest {
    @NotBlank(message = "비밀번호 변경 허가증(토큰)이 없습니다.")
    private String resetToken;

    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    private String newPassword;
}