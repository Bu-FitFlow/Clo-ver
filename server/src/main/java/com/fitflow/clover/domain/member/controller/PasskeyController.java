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

    @Operation(summary = "패스키 등록 시작 (로그인 상태)", description = "새로운 패스키(생체 인증 기기) 등록을 위한 WebAuthn 옵션 데이터(Challenge 등)를 생성하여 클라이언트(앱)로 전달합니다.")
    @GetMapping("/passkey/register/start")
    public ResponseEntity<String> startPasskeyRegistration(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String optionsJson = passkeyService.startRegistration(memberId);
        return ResponseEntity.ok(optionsJson);
    }

    @Operation(summary = "패스키 등록 완료", description = "기기에서 생성된 패스키 증명 데이터를 서버에서 검증하고, 성공 시 사용자의 새로운 패스키로 최종 등록합니다.")
    @PostMapping("/passkey/register/finish")
    public ResponseEntity<String> finishPasskeyRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody String responseJson) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        passkeyService.finishRegistration(memberId, responseJson);
        return ResponseEntity.ok("패스키(지문/Face ID)가 완벽하게 등록되었습니다!");
    }

    @Operation(summary = "패스키 로그인 시작", description = "패스키 로그인을 시작하기 위해 필요한 WebAuthn 옵션 데이터(Challenge 등)를 생성하여 클라이언트(앱)로 전달합니다.")
    @GetMapping("/passkey/login/start")
    public ResponseEntity<String> startPasskeyLogin(@RequestParam String loginId) {
        String optionsJson = passkeyService.startAuthentication(loginId);
        return ResponseEntity.ok(optionsJson);
    }

    @Operation(summary = "패스키 로그인 완료", description = "기기에서 서명된 패스키 인증 데이터를 서버에서 검증하고, 성공 시 JWT 토큰(Access/Refresh)을 발급하여 로그인 처리합니다.")
    @PostMapping("/passkey/login/finish")
    public ResponseEntity<TokenResponse> finishPasskeyLogin(
            @RequestParam String loginId,
            @RequestBody String responseJson) {

        TokenResponse tokenResponse = passkeyService.finishAuthentication(loginId, responseJson);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "내 패스키 목록 조회 (로그인 상태)", description = "현재 로그인된 사용자의 계정에 등록된 모든 패스키(기기) 목록을 조회합니다.")
    @GetMapping("/me/passkeys")
    public ResponseEntity<List<PasskeyResponse>> getMyPasskeys(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        List<PasskeyResponse> response = passkeyService.getMyPasskeys(memberId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "등록된 패스키 기기 삭제 (로그인 상태)", description = "사용자가 등록했던 특정 패스키(기기)를 삭제합니다. 삭제된 기기로는 더 이상 패스키 로그인을 할 수 없습니다.")
    @DeleteMapping("/me/passkeys/{passkeyId}")
    public ResponseEntity<String> deletePasskey(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long passkeyId) {

        Long memberId = Long.parseLong(userDetails.getUsername());

        passkeyService.deletePasskey(memberId, passkeyId);

        return ResponseEntity.ok("등록된 패스키 기기가 성공적으로 삭제되었습니다.");
    }
}