package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.dto.request.LoginRequest;
import com.fitflow.clover.domain.member.dto.response.TokenResponse;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.security.JwtTokenProvider;
import com.fitflow.clover.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final TotpService totpService;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (!member.isEmailVerified()) {
            if (mailService.isVerificationEmailSent(member.getEmail())) {
                throw new CustomException(ErrorCode.EMAIL_VERIFICATION_PENDING);
            } else {
                mailService.sendVerificationEmail(member.getEmail());
                throw new CustomException(ErrorCode.EMAIL_VERIFICATION_RESENT);
            }
        }

        if (member.isTotpEnabled()) {
            if (request.getTotpCode() == null || request.getTotpCode().isEmpty()) {
                throw new CustomException(ErrorCode.TOTP_VERIFICATION_REQUIRED);
            }
            totpService.verifyCode(member.getTotpSecret(), request.getTotpCode());
        }

        return jwtTokenProvider.issueTokenResponse(member.getMemberId(), member.getRole());
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String memberId = jwtTokenProvider.getUserId(refreshToken);

        String savedToken = redisUtil.getData("RT:" + memberId);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(member.getMemberId(), member.getRole());

        return new TokenResponse(newAccessToken, refreshToken);
    }
}
