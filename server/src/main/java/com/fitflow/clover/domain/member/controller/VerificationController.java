package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이메일 인증", description = "회원가입 등 이메일 소유권 인증 관련 API")
@RestController
@RequestMapping("/api/members/emails")
@RequiredArgsConstructor
public class VerificationController {
    private final MailService mailService;

    @Operation(summary = "인증 메일 발송", description = "회원가입 등 이메일 소유권 확인이 필요한 경우, 입력한 이메일 주소로 고유한 인증 토큰이 포함된 확인 링크를 발송합니다.")
    @PostMapping("/verification-requests")
    public ResponseEntity<String> sendMessage(@RequestParam("email") String email) {
        mailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 메일이 성공적으로 발송되었습니다.");
    }

    @Operation(summary = "인증 메일 링크 검증", description = "사용자가 이메일에서 수신한 인증 링크를 클릭했을 때 호출됩니다. 전달된 토큰과 이메일의 유효성을 검증하고, 성공 여부에 따라 결과를 HTML 형식으로 반환하여 웹 브라우저에 표시합니다.")
    @GetMapping(value = "/verify", produces = "text/html; charset=UTF-8")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        boolean isVerified = mailService.verifyEmail(email, token);

        if (isVerified) return ResponseEntity.ok(getHtmlTemplate(true));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getHtmlTemplate(false));
    }

    // TODO: 회원가입 이메일 인증 디자인
    private String getHtmlTemplate(boolean isSuccess) {
        String title = isSuccess ? "인증 완료" : "인증 실패";
        String icon = isSuccess ? "✅" : "❌";
        String color = isSuccess ? "#2ECC71" : "#E74C3C";
        String message = isSuccess ? "이메일 인증이 완벽하게 처리되었습니다!<br>이 창을 닫고 앱으로 돌아가 가입을 계속해주세요."
                : "인증 링크가 만료되었거나 올바르지 않습니다.<br>앱에서 다시 인증 메일을 요청해주세요.";

        return "<!DOCTYPE html>" +
                "<html lang='ko'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>CLO-VER 이메일 인증</title>" +
                "<style>" +
                "body { font-family: -apple-system, BlinkMacSystemFont, 'Apple SD Gothic Neo', sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".card { background: white; padding: 40px 30px; border-radius: 16px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); text-align: center; max-width: 400px; width: 85%; }" +
                ".icon { font-size: 60px; margin-bottom: 20px; }" +
                "h1 { color: #333; font-size: 24px; margin-bottom: 15px; font-weight: bold; }" +
                "p { color: #666; font-size: 16px; line-height: 1.6; margin-bottom: 0; }" +
                ".highlight { color: " + color + "; font-weight: bold; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='card'>" +
                "<div class='icon'>" + icon + "</div>" +
                "<h1>" + title + "</h1>" +
                "<p>" + message + "</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}