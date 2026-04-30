package com.fitflow.clover.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PasskeyResponse {
    private Long id;

    private String credentialId;

    private Long signCount;

    private LocalDateTime createdAt;
}