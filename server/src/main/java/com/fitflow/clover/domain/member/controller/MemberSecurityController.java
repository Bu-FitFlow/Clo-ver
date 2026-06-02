package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.request.*;
import com.fitflow.clover.domain.member.dto.response.TotpStatusResponse;
import com.fitflow.clover.domain.member.service.MemberService;
import com.fitflow.clover.domain.member.service.TotpService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 보안 및 계정 찾기", description = "아이디/비밀번호 찾기, 비밀번호 변경, 2차 인증(TOTP) 설정 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberSecurityController {
    private final MemberService memberService;
    private final TotpService totpService;

    @Operation(summary = "아이디 찾기 - 인증메일 발송", description = "가입 시 등록한 이메일로 아이디 찾기를 위한 6자리 인증번호를 발송합니다.")
    @PostMapping("/find-id/send")
    public ResponseEntity<String> sendFindIdCode(@RequestBody @Valid FindIdSendRequest request) {
        memberService.sendFindIdCode(request);
        return ResponseEntity.ok("입력하신 이메일로 인증번호가 발송되었습니다.");
    }

    @Operation(summary = "아이디 찾기 - 인증번호 검증", description = "이메일로 발송된 6자리 인증번호가 유효한지 검증합니다. 검증 성공 시 마스킹 처리된 아이디를 반환합니다.")
    @PostMapping("/find-id/verify")
    public ResponseEntity<String> verifyFindIdCode(@RequestBody @Valid FindIdVerifyRequest request) {
        String loginId = memberService.verifyFindIdCode(request);
        return ResponseEntity.ok(loginId);
    }

    @Operation(summary = "내 비밀번호 변경 (로그인 상태)", description = "기존 비밀번호를 확인한 후, 새로운 비밀번호로 변경합니다.")
    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordChangeRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.changePassword(memberId, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다. 새로운 비밀번호로 다시 로그인해 주세요.");
    }

    @Operation(summary = "비밀번호 찾기 - 인증메일 발송", description = "비밀번호 재설정을 위해 가입된 이메일로 6자리 인증번호를 발송합니다.")
    @PostMapping("/password/find/send")
    public ResponseEntity<String> sendPasswordResetCode(@RequestBody @Valid PasswordResetSendRequest request) {
        memberService.sendPasswordResetCode(request);
        return ResponseEntity.ok("입력하신 이메일로 인증번호가 발송되었습니다.");
    }

    @Operation(summary = "비밀번호 찾기 - 인증번호 검증", description = "이메일로 발송된 6자리 인증번호가 유효한지 검증합니다. 검증 성공 시 재설정을 위한 임시 토큰(허가증)을 반환합니다.")
    @PostMapping("/password/find/verify")
    public ResponseEntity<String> verifyPasswordResetCode(@RequestBody @Valid PasswordResetVerifyRequest request) {
        // 성공 시 resetToken(허가증) 반환
        String resetToken = memberService.verifyPasswordResetCode(request);
        return ResponseEntity.ok(resetToken);
    }

    @Operation(summary = "비밀번호 재설정 (계정 찾기 후)", description = "이메일 인증을 통해 발급받은 임시 토큰(허가증)을 사용하여 비밀번호를 새롭게 설정합니다.")
    @PatchMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다. 새로운 비밀번호로 로그인해 주세요.");
    }

    @Operation(summary = "2차 인증(TOTP) QR 코드 발급", description = "Google Authenticator 등 OTP 앱에 등록할 수 있는 2차 인증용 QR 코드 이미지(Base64)를 발급합니다.")
    @GetMapping("/totp/setup")
    public ResponseEntity<String> setupTotp(@AuthenticationPrincipal UserDetails userDetails) throws QrGenerationException {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String qrCodeImage = totpService.generateQrCode(memberId);

        return ResponseEntity.ok(qrCodeImage);
    }

    @Operation(summary = "2차 인증(TOTP) 활성화", description = "OTP 앱에 표시된 6자리 코드를 검증하여 계정의 2차 인증(TOTP)을 활성화합니다.")
    @PostMapping("/totp/enable")
    public ResponseEntity<String> enableTotp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TotpEnableRequest request) { // 우리가 만든 DTO 사용

        Long memberId = Long.parseLong(userDetails.getUsername());
        totpService.enableTotp(memberId, request.code());

        return ResponseEntity.ok("2차 인증이 성공적으로 활성화되었습니다.");
    }

    @Operation(summary = "2차 인증(TOTP) 해제", description = "계정에 설정된 2차 인증(TOTP) 기능을 완전히 해제합니다.")
    @DeleteMapping("/totp/disable")
    public ResponseEntity<String> disableTotp(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        totpService.disableTotp(memberId);

        return ResponseEntity.ok("2차 인증이 성공적으로 해제되었습니다.");
    }

    @Operation(summary = "2차 인증(TOTP) 상태 조회", description = "현재 로그인한 사용자의 2차 인증(TOTP) 활성화 여부를 조회합니다.")
    @GetMapping("/me/totp/status")
    public ResponseEntity<TotpStatusResponse> getTotpStatus(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        TotpStatusResponse response = memberService.getTotpStatus(memberId);

        return ResponseEntity.ok(response);
    }
}
