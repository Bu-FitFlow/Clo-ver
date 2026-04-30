package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.response.PasskeyResponse;
import com.fitflow.clover.domain.member.dto.response.TokenResponse;
import com.fitflow.clover.domain.member.service.PasskeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class PasskeyController {
    private final PasskeyService passkeyService;

    @GetMapping("/passkey/register/start")
    public ResponseEntity<String> startPasskeyRegistration(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String optionsJson = passkeyService.startRegistration(memberId);
        return ResponseEntity.ok(optionsJson);
    }

    @PostMapping("/passkey/register/finish")
    public ResponseEntity<String> finishPasskeyRegistration(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody String responseJson) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        passkeyService.finishRegistration(memberId, responseJson);
        return ResponseEntity.ok("패스키(지문/Face ID)가 완벽하게 등록되었습니다!");
    }

    @GetMapping("/passkey/login/start")
    public ResponseEntity<String> startPasskeyLogin(@RequestParam String loginId) {
        String optionsJson = passkeyService.startAuthentication(loginId);
        return ResponseEntity.ok(optionsJson);
    }

    @PostMapping("/passkey/login/finish")
    public ResponseEntity<TokenResponse> finishPasskeyLogin(
            @RequestParam String loginId,
            @RequestBody String responseJson) {

        TokenResponse tokenResponse = passkeyService.finishAuthentication(loginId, responseJson);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/me/passkeys")
    public ResponseEntity<List<PasskeyResponse>> getMyPasskeys(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        List<PasskeyResponse> response = passkeyService.getMyPasskeys(memberId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/passkeys/{passkeyId}")
    public ResponseEntity<String> deletePasskey(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long passkeyId) {

        Long memberId = Long.parseLong(userDetails.getUsername());

        passkeyService.deletePasskey(memberId, passkeyId);

        return ResponseEntity.ok("등록된 패스키 기기가 성공적으로 삭제되었습니다.");
    }
}
