package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberWithdrawRequest {
    @NotBlank(message = "비밀번호 확인이 필요합니다.")
    private String password;
}
