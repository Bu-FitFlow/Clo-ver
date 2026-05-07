package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.dto.request.*;
import com.fitflow.clover.domain.member.dto.response.MemberInfoResponse;
import com.fitflow.clover.domain.member.dto.response.MemberResponse;
import com.fitflow.clover.domain.member.dto.response.TotpStatusResponse;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.member.repository.PasskeyRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.infra.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private static final String FIND_ID_PREFIX = "FIND_ID:";
    private static final long VERIFY_TIME_LIMIT = 600000L;
    private static final String PWD_RESET_PREFIX = "PWD_RESET:";
    private static final String PWD_RESET_TOKEN_PREFIX = "PWD_RESET_TOKEN:";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasskeyRepository passkeyRepository;
    private final MailService mailService;
    private final RedisUtil redisUtil;

    @Transactional
    public MemberResponse signUp(SignUpRequest request) {
        checkDuplicateMember(request);

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.builder()
                .loginId(request.loginId())
                .password(encodedPassword)
                .name(request.name())
                .nickname(request.nickname())
                .email(request.email())
                .isEmailVerified(false)
                .gender(request.gender())
                .build();

        Member savedMember = memberRepository.save(member);

        mailService.sendVerificationEmail(member.getEmail());

        return MemberResponse.from(savedMember);
    }

    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean hasPasskey = passkeyRepository.existsByMember(member);

        return MemberInfoResponse.from(member, hasPasskey);
    }

    @Transactional
    public void updateMyInfo(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!member.getEmail().equals(request.email())) {
            String verifiedKey = "VERIFIED_EMAIL:" + request.email();
            String isVerified = redisUtil.getData(verifiedKey);

            if (isVerified == null || !isVerified.equals("true")) {
                throw new CustomException(ErrorCode.UNVERIFIED_EMAIL);
            }
            redisUtil.deleteData(verifiedKey);
        }
        member.updateProfile(request.nickname(), request.email());
    }

    @Transactional
    public void changePassword(Long memberId, PasswordChangeRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(request.newPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());
        member.updatePassword(encodedNewPassword);

        redisUtil.deleteData("RT:" + memberId);
    }

    public void sendFindIdCode(FindIdSendRequest request) {
        Member member = memberRepository.findByNameAndEmail(request.name(), request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String authCode = String.valueOf((int) (Math.random() * 899999) + 100000);

        redisUtil.setDataExpire(FIND_ID_PREFIX + request.email(), authCode, VERIFY_TIME_LIMIT);

        mailService.sendAuthCodeEmail(member.getEmail(), authCode);
    }

    public String verifyFindIdCode(FindIdVerifyRequest request) {
        String savedCode = redisUtil.getData(FIND_ID_PREFIX + request.email());

        if (savedCode == null || !savedCode.equals(request.code())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        Member member = memberRepository.findByNameAndEmail(request.name(), request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        redisUtil.deleteData(FIND_ID_PREFIX + request.email());

        return member.getLoginId();
    }

    public void sendPasswordResetCode(PasswordResetSendRequest request) {
        memberRepository.findByLoginIdAndNameAndEmail(request.loginId(), request.name(), request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String authCode = String.valueOf((int) (Math.random() * 899999) + 100000);

        redisUtil.setDataExpire(PWD_RESET_PREFIX + request.email(), authCode, VERIFY_TIME_LIMIT);

        mailService.sendAuthCodeEmail(request.email(), authCode);
    }

    public String verifyPasswordResetCode(PasswordResetVerifyRequest request) {
        String savedCode = redisUtil.getData(PWD_RESET_PREFIX + request.email());

        if (savedCode == null || !savedCode.equals(request.code())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisUtil.deleteData(PWD_RESET_PREFIX + request.email());

        String resetToken = java.util.UUID.randomUUID().toString();

        redisUtil.setDataExpire(PWD_RESET_TOKEN_PREFIX + resetToken, request.loginId(), 300000L);

        return resetToken;
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        String loginId = redisUtil.getData(PWD_RESET_TOKEN_PREFIX + request.resetToken());

        if (loginId == null) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(request.newPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        member.updatePassword(passwordEncoder.encode(request.newPassword()));

        redisUtil.deleteData(PWD_RESET_TOKEN_PREFIX + request.resetToken());

        redisUtil.deleteData("RT:" + member.getMemberId());
    }

    public TotpStatusResponse getTotpStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new TotpStatusResponse(member.isTotpEnabled());
    }

    @Transactional
    public void withdraw(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        member.withdraw();

        redisUtil.deleteData("RT:" + memberId);
    }

    private void checkDuplicateMember(SignUpRequest request) {
        if (memberRepository.existsByLoginId(request.loginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
        if (memberRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (memberRepository.existsByNickname(request.nickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    public boolean checkLoginIdAvailable(String loginId) {
        return !memberRepository.existsByLoginId(loginId);
    }

    public boolean checkEmailAvailable(String email) {
        return !memberRepository.existsByEmail(email);
    }

    public boolean checkNicknameAvailable(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }
}
