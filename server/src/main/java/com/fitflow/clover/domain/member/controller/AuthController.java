package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.request.LoginRequest;
import com.fitflow.clover.domain.member.dto.response.TokenResponse;
import com.fitflow.clover.domain.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 인증", description = "로그인, 로그아웃, 토큰 재발급 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인", description = "사용자의 아이디와 비밀번호를 검증하고, 성공 시 Access Token과 Refresh Token을 발급합니다. (만약 2차 인증(TOTP)이 설정된 계정이라면 추가 인증 요구 상태를 반환합니다.)")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 Accss Token을 헤더의 Refresh Token을 이용해 재발급 받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization-Refresh") String refreshToken) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "로그아웃 (로그인 상태)", description = "현재 로그인된 사용자의 Refresh Token을 서버(Redis 등)에서 무효화하여 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        authService.logout(memberId);
        return ResponseEntity.ok("성공적으로 로그아웃되었습니다.");
    }
}
