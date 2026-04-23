package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    public void sendVerificationEmail(String toEmail) {
        String token = UUID.randomUUID().toString();

        redisUtil.setDataExpire(toEmail, token, 60 * 10L * 1000);

        String verificationLink = "http://localhost:8080/api/members/emails/verify?email=" + toEmail + "&token=" + token;

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
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
}
