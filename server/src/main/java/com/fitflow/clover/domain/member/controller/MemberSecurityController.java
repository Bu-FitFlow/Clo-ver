package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.request.*;
import com.fitflow.clover.domain.member.dto.response.TotpStatusResponse;
import com.fitflow.clover.domain.member.service.MemberService;
import com.fitflow.clover.domain.member.service.TotpService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberSecurityController {
    private final MemberService memberService;
    private final TotpService totpService;

    @PostMapping("/find-id/send")
    public ResponseEntity<String> sendFindIdCode(@RequestBody @Valid FindIdSendRequest request) {
        memberService.sendFindIdCode(request);
        return ResponseEntity.ok("입력하신 이메일로 인증번호가 발송되었습니다.");
    }

    @PostMapping("/find-id/verify")
    public ResponseEntity<String> verifyFindIdCode(@RequestBody @Valid FindIdVerifyRequest request) {
        String loginId = memberService.verifyFindIdCode(request);
        return ResponseEntity.ok(loginId);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordChangeRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.changePassword(memberId, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다. 새로운 비밀번호로 다시 로그인해 주세요.");
    }

    @PostMapping("/password/find/send")
    public ResponseEntity<String> sendPasswordResetCode(@RequestBody @Valid PasswordResetSendRequest request) {
        memberService.sendPasswordResetCode(request);
        return ResponseEntity.ok("입력하신 이메일로 인증번호가 발송되었습니다.");
    }

    @PostMapping("/password/find/verify")
    public ResponseEntity<String> verifyPasswordResetCode(@RequestBody @Valid PasswordResetVerifyRequest request) {
        // 성공 시 resetToken(허가증) 반환
        String resetToken = memberService.verifyPasswordResetCode(request);
        return ResponseEntity.ok(resetToken);
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다. 새로운 비밀번호로 로그인해 주세요.");
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

    @GetMapping("/me/totp/status")
    public ResponseEntity<TotpStatusResponse> getTotpStatus(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        TotpStatusResponse response = memberService.getTotpStatus(memberId);

        return ResponseEntity.ok(response);
    }
}
