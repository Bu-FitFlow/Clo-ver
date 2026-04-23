package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.MemberResponse;
import com.fitflow.clover.domain.member.dto.SignUpRequest;
import com.fitflow.clover.domain.member.dto.TokenResponse;
import com.fitflow.clover.domain.member.dto.TotpEnableRequest;
import com.fitflow.clover.domain.member.service.MemberService;
import com.fitflow.clover.domain.member.service.PasskeyService;
import com.fitflow.clover.domain.member.service.TotpService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TotpService totpService;
    private final PasskeyService passkeyService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String memberId = userDetails.getUsername();
        return ResponseEntity.ok("환영합니다! 당신의 회원 식별 번호는 " + memberId + " 입니다.");
    }

    @GetMapping("/totp/setup")
    public ResponseEntity<String> setupTotp(@AuthenticationPrincipal UserDetails userDetails) throws QrGenerationException {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String qrCodeImage = totpService.generateQrCode(memberId);

        return ResponseEntity.ok(qrCodeImage);
    }

    @PostMapping("/totp/enable")
    public ResponseEntity<String> enableTotp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TotpEnableRequest request) { // 우리가 만든 DTO 사용

        Long memberId = Long.parseLong(userDetails.getUsername());
        totpService.enableTotp(memberId, request.getCode());

        return ResponseEntity.ok("2차 인증이 성공적으로 활성화되었습니다.");
    }

    @DeleteMapping("/totp/disable")
    public ResponseEntity<String> disableTotp(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        totpService.disableTotp(memberId);

        return ResponseEntity.ok("2차 인증이 성공적으로 해제되었습니다.");
    }

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
}
