package com.fitflow.clover.domain.member.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String loginId;
    private String password;
    private String totpCode;
}
