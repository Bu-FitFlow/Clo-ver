package com.fitflow.clover.domain.admin.dto.request;

public record AdminSignUpRequest(
        String loginId,
        String password,
        String name,
        String email
) {
}