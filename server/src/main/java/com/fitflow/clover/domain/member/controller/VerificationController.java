package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이메일 인증", description = "회원가입 등 이메일 소유권 인증 관련 API")
@RestController
@RequestMapping("/api/members/emails")
@RequiredArgsConstructor
public class VerificationController {
    private final MailService mailService;

    @Operation(summary = "인증 메일 발송")
    @PostMapping("/verification-requests")
    public ResponseEntity<String> sendMessage(@RequestParam("email") String email) {
        mailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 메일이 성공적으로 발송되었습니다.");
    }

    @Operation(summary = "인증 메일 링크 검증")
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        boolean isVerified = mailService.verifyEmail(email, token);

        if (isVerified) return ResponseEntity.ok("이메일 인증이 완벽하게 처리되었습니다! 창을 닫고 가입을 계속해주세요.");
        else return ResponseEntity.badRequest().body("인증 링크가 만료되었거나 올바르지 않습니다.");
    }
}