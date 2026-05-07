package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.infra.redis.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.mail.from.verification}")
    private String verificationSender;

    public void sendVerificationEmail(String toEmail) {
        String token = UUID.randomUUID().toString();

        redisUtil.setDataExpire(toEmail, token, 60 * 10L * 1000);

        String verificationLink = baseUrl + "/api/members/emails/verify?email=" + toEmail + "&token=" + token;

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(verificationSender);
            helper.setTo(toEmail);
            helper.setSubject("[Clo-ver] 중고 의류 플랫폼 회원가입 이메일 인증");

            String htmlContent = "<h3>Clo-ver 가입을 환영합니다!</h3>" +
                    "<p>아래 버튼을 클릭하여 이메일 인증을 완료해주세요. (10분 내에 클릭해야 합니다)</p>" +
                    "<a href='" + verificationLink + "' style='display:inline-block; padding:10px 20px; background-color:#28a745; color:white; text-decoration:none; border-radius:5px;'>인증하기</a>";

            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    @Transactional
    public boolean verifyEmail(String email, String token) {
        String savedToken = redisUtil.getData(email);

        if (savedToken != null && savedToken.equals(token)) {
            redisUtil.deleteData(email);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            member.verifyEmail();
            return true;
        }
        return false;
    }

    // TODO: 회원가입 이메일 인증 디자인
    public void sendAuthCodeEmail(String toEmail, String authCode) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(verificationSender);
            helper.setTo(toEmail);
            helper.setSubject("[Clo-ver] 계정 찾기 인증번호 안내");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;'>" +
                    "<h3 style='color: #333;'>Clo-ver 계정 찾기 인증번호</h3>" +
                    "<p style='color: #555; line-height: 1.5;'>요청하신 인증번호를 안내해 드립니다.<br>아래 6자리 숫자를 진행 중인 화면에 입력해 주세요.</p>" +
                    "<div style='display:inline-block; padding:15px 30px; margin-top:10px; background-color:#f8f9fa; border:1px solid #dee2e6; border-radius:5px; font-size:28px; font-weight:bold; letter-spacing:10px; color:#28a745;'>" +
                    authCode + "</div>" +
                    "<p style='margin-top:20px; color:#999; font-size:12px;'>* 본 인증번호는 발송 시점으로부터 10분 동안만 유효합니다.<br>* 본인이 요청하지 않은 경우 이 메일을 무시해 주세요.</p>" +
                    "</div>";

            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("인증번호 이메일 발송에 실패했습니다.", e);
        }
    }

    public boolean isVerificationEmailSent(String email) {
        return redisUtil.getData(email) != null;
    }
}
