package com.fitflow.clover.domain.member.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        boolean isFirstLogin
) {
}
