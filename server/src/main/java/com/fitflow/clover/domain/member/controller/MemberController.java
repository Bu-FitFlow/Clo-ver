package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.request.*;
import com.fitflow.clover.domain.member.dto.response.*;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관리", description = "회원가입, 정보 조회/수정, 탈퇴 및 중복 검사 관련 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        // memberRepository.existsByLoginId(loginId) 결과를 반대로 뒤집어서 리턴
        boolean isAvailable = !memberRepository.existsByLoginId(loginId);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "이메일 중복 확인")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean isAvailable = memberService.checkEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "닉네임 중복 확인")
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = !memberRepository.existsByNickname(nickname);
        return ResponseEntity.ok(isAvailable);
    }

    @Operation(summary = "내 정보 조회 (로그인 상태)")
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        MemberInfoResponse response = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정 (로그인 상태)")
    @PatchMapping("/me")
    public ResponseEntity<String> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberUpdateRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.updateMyInfo(memberId, request);
        return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "회원 탈퇴 (로그인 상태)")
    @DeleteMapping("/me")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberWithdrawRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.withdraw(memberId, request.password());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다. 그동안 이용해 주셔서 감사합니다.");
    }
}
