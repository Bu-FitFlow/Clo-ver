package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.dto.request.MemberUpdateRequest;
import com.fitflow.clover.domain.member.dto.response.MemberInfoResponse;
import com.fitflow.clover.domain.member.dto.response.MemberResponse;
import com.fitflow.clover.domain.member.dto.request.SignUpRequest;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.member.repository.PasskeyRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasskeyRepository passkeyRepository;
    private final MailService mailService;
    private final RedisUtil redisUtil;


    @Transactional
    public MemberResponse signUp(SignUpRequest request) {
        checkDuplicateMember(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .isEmailVerified(false)
                .gender(request.getGender())
                .build();

        Member savedMember = memberRepository.save(member);

        mailService.sendVerificationEmail(member.getEmail());

        return MemberResponse.from(savedMember);
    }

    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean hasPasskey = !passkeyRepository.findAllByMember_MemberId(memberId).isEmpty();

        return MemberInfoResponse.builder()
                .loginId(member.getLoginId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .totpEnabled(member.isTotpEnabled())
                .hasPasskey(hasPasskey)
                .build();
    }

    @Transactional
    public void updateMyInfo(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!member.getEmail().equals(request.getEmail())) {
            String verifiedKey = "VERIFIED_EMAIL:" + request.getEmail();
            String isVerified = redisUtil.getData(verifiedKey);

            if (isVerified == null || !isVerified.equals("true")) {
                throw new CustomException(ErrorCode.UNVERIFIED_EMAIL);
            }
            redisUtil.deleteData(verifiedKey);
        }
        member.updateProfile(request.getNickname(), request.getEmail());
    }

    private void checkDuplicateMember(SignUpRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
