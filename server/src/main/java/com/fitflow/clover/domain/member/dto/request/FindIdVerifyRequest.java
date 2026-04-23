package com.fitflow.clover.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindIdVerifyRequest {
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "인증번호를 입력해 주세요.")
    private String code;
}