package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.response.PasskeyResponse;
import com.fitflow.clover.domain.member.dto.response.TokenResponse;
import com.fitflow.clover.domain.member.service.PasskeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "패스키 인증 및 관리", description = "WebAuthn 기반 패스키(지문, Face ID 등) 등록, 로그인 및 기기 관리 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class PasskeyController {
    private final PasskeyService passkeyService;

    @Operation(summary = "패스키 등록 시작 (로그인 상태)")
    @GetMapping("/passkey/register/start")
    public ResponseEntity<String> startPasskeyRegistration(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String optionsJson = passkeyService.startRegistration(memberId);
        return ResponseEntity.ok(optionsJson);
    }

    @Operation(summary = "패스키 등록 완료")
    @PostMapping("/passkey/register/finish")
    public ResponseEntity<String> finishPasskeyRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody String responseJson) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        passkeyService.finishRegistration(memberId, responseJson);
        return ResponseEntity.ok("패스키(지문/Face ID)가 완벽하게 등록되었습니다!");
    }

    @Operation(summary = "패스키 로그인 시작")
    @GetMapping("/passkey/login/start")
    public ResponseEntity<String> startPasskeyLogin(@RequestParam String loginId) {
        String optionsJson = passkeyService.startAuthentication(loginId);
        return ResponseEntity.ok(optionsJson);
    }

    @Operation(summary = "패스키 로그인 완료")
    @PostMapping("/passkey/login/finish")
    public ResponseEntity<TokenResponse> finishPasskeyLogin(
            @RequestParam String loginId,
            @RequestBody String responseJson) {

        TokenResponse tokenResponse = passkeyService.finishAuthentication(loginId, responseJson);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "내 패스키 목록 조회 (로그인 상태)")
    @GetMapping("/me/passkeys")
    public ResponseEntity<List<PasskeyResponse>> getMyPasskeys(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        List<PasskeyResponse> response = passkeyService.getMyPasskeys(memberId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "등록된 패스키 기기 삭제 (로그인 상태)")
    @DeleteMapping("/me/passkeys/{passkeyId}")
    public ResponseEntity<String> deletePasskey(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long passkeyId) {

        Long memberId = Long.parseLong(userDetails.getUsername());

        passkeyService.deletePasskey(memberId, passkeyId);

        return ResponseEntity.ok("등록된 패스키 기기가 성공적으로 삭제되었습니다.");
    }
}