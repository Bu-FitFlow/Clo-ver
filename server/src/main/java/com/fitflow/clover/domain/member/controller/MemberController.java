package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.request.*;
import com.fitflow.clover.domain.member.dto.response.*;
import com.fitflow.clover.domain.member.repository.MemberRepository;
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

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TotpService totpService;
    private final PasskeyService passkeyService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        // memberRepository.existsByLoginId(loginId) 결과를 반대로 뒤집어서 리턴
        boolean isAvailable = !memberRepository.existsByLoginId(loginId);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = !memberRepository.existsByNickname(nickname);
        return ResponseEntity.ok(isAvailable);
    }

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

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        MemberInfoResponse response = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/me/passkeys")
    public ResponseEntity<List<PasskeyResponse>> getMyPasskeys(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        List<PasskeyResponse> response = passkeyService.getMyPasskeys(memberId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/totp/status")
    public ResponseEntity<TotpStatusResponse> getTotpStatus(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        TotpStatusResponse response = memberService.getTotpStatus(memberId);

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

    @PatchMapping("/me")
    public ResponseEntity<String> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberUpdateRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.updateMyInfo(memberId, request);
        return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordChangeRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.changePassword(memberId, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다. 새로운 비밀번호로 다시 로그인해 주세요.");
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberWithdrawRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.withdraw(memberId, request.getPassword());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다. 그동안 이용해 주셔서 감사합니다.");
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
