package com.fitflow.clover.domain.member.dto.response;

import java.time.LocalDateTime;

public record PasskeyResponse(
        Long id,
        String credentialId,
        Long signCount,
        LocalDateTime createdAt
) {
}