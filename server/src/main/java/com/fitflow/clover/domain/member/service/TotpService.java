package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TotpService {
    private final MemberRepository memberRepository;
    private final SecretGenerator secretGenerator;
    private final QrGenerator qrGenerator;
    private final CodeVerifier codeVerifier;

    @Transactional
    public String generateQrCode(Long memberId) throws QrGenerationException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String secret = secretGenerator.generate();
        member.updateTotpSecret(secret);

        QrData data = new QrData.Builder()
                .label(member.getEmail())
                .secret(secret)
                .issuer("Clo-ver")
                .build();

        byte[] imageData = qrGenerator.generate(data);
        String mimeType = qrGenerator.getImageMimeType();

        return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageData);
    }

    public void verifyCode(String secret, String code) {
        if (!codeVerifier.isValidCode(secret, code)) {
            throw new CustomException(ErrorCode.INVALID_TOTP_CODE);
        }
    }

    @Transactional
    public void enableTotp(Long memberId, String code) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!codeVerifier.isValidCode(member.getTotpSecret(), code)) {
            throw new CustomException(ErrorCode.INVALID_TOTP_CODE);
        }

        member.enableTotp();
    }

    @Transactional
    public void disableTotp(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        member.disableTotp();
    }
}
