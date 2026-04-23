package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/emails")
@RequiredArgsConstructor
public class VerificationController {
    private final MailService mailService;

    @PostMapping("/verification-requests")
    public ResponseEntity<String> sendMessage(@RequestParam("email") String email) {
        mailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 메일이 성공적으로 발송되었습니다.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        boolean isVerified = mailService.verifyEmail(email, token);

        if (isVerified) return ResponseEntity.ok("이메일 인증이 완벽하게 처리되었습니다! 창을 닫고 가입을 계속해주세요.");
        else return ResponseEntity.badRequest().body("인증 링크가 만료되었거나 올바르지 않습니다.");
    }
}
