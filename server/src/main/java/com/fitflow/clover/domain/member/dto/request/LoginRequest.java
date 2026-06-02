package com.fitflow.clover.domain.member.dto.request;

public record LoginRequest(
        String loginId,
        String totpCode,
        String password
) {
}
