package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.dto.response.TokenResponse;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.entity.PasskeyCredential;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.member.repository.PasskeyRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.security.JwtTokenProvider;
import com.fitflow.clover.global.util.RedisUtil;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import lombok.RequiredArgsConstructor;
import com.yubico.webauthn.data.PublicKeyCredential;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PasskeyService {
    private static final String REG_REQ_PREFIX = "PASSKEY_REG:";
    private static final String AUTH_REQ_PREFIX = "PASSKEY_AUTH:";
    private static final long EXPIRE_MILLIS = 300000L;
    private final RedisUtil redisUtil;
    private final RelyingParty relyingParty;
    private final MemberRepository memberRepository;
    private final PasskeyRepository passkeyRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private void saveRegistrationRequest(String loginId, PublicKeyCredentialCreationOptions request) {
        try {
            String jsonRequest = request.toJson();
            redisUtil.setDataExpire(REG_REQ_PREFIX + loginId, jsonRequest, EXPIRE_MILLIS);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    public PublicKeyCredentialCreationOptions getRegistrationRequest(String loginId) {
        String key = REG_REQ_PREFIX + loginId;
        String jsonRequest = redisUtil.getData(key);
        if (jsonRequest == null) throw new CustomException(ErrorCode.PASSKEY_TIMEOUT);

        redisUtil.deleteData(key);

        try {
            return PublicKeyCredentialCreationOptions.fromJson(jsonRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    private void saveAuthenticationRequest(String loginId, AssertionRequest request) {
        try {
            String jsonRequest = request.toJson();
            redisUtil.setDataExpire(AUTH_REQ_PREFIX + loginId, jsonRequest, EXPIRE_MILLIS);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    public AssertionRequest getAuthenticationRequest(String loginId) {
        String key = AUTH_REQ_PREFIX + loginId;
        String jsonRequest = redisUtil.getData(key);
        if (jsonRequest == null) {
            throw new CustomException(ErrorCode.PASSKEY_TIMEOUT);
        }

        redisUtil.deleteData(key);

        try {
            return AssertionRequest.fromJson(jsonRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    public String startRegistration(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        byte[] userHandle = String.valueOf(memberId).getBytes();

        UserIdentity userIdentity = UserIdentity.builder()
                .name(member.getLoginId())
                .displayName(member.getLoginId())
                .id(new ByteArray(userHandle))
                .build();

        StartRegistrationOptions startOptions = StartRegistrationOptions.builder()
                .user(userIdentity)
                .build();

        try {
            PublicKeyCredentialCreationOptions options = relyingParty.startRegistration(startOptions);
            saveRegistrationRequest(String.valueOf(memberId), options);
            return options.toCredentialsCreateJson();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    @Transactional
    public void finishRegistration(Long memberId, String responseJson) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PublicKeyCredentialCreationOptions requestOptions = getRegistrationRequest(String.valueOf(memberId));

        try {
            PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc = PublicKeyCredential.parseRegistrationResponseJson(responseJson);
            FinishRegistrationOptions options = FinishRegistrationOptions.builder()
                    .request(requestOptions)
                    .response(pkc)
                    .build();

            RegistrationResult result = relyingParty.finishRegistration(options);

            PasskeyCredential credential = PasskeyCredential.builder()
                    .member(member)
                    .credentialId(result.getKeyId().getId().getBytes())
                    .publicKey(result.getPublicKeyCose().getBytes())
                    .signCount(result.getSignatureCount())
                    .userHandle(String.valueOf(memberId).getBytes())
                    .build();

            passkeyRepository.save(credential);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    public String startAuthentication(String loginId) {
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        StartAssertionOptions startOptions = StartAssertionOptions.builder()
                .username(loginId)
                .build();

        try {
            AssertionRequest assertionRequest = relyingParty.startAssertion(startOptions);

            saveAuthenticationRequest(loginId, assertionRequest);
            return assertionRequest.getPublicKeyCredentialRequestOptions().toCredentialsGetJson();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }

    @Transactional
    public TokenResponse finishAuthentication(String loginId, String responseJson) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        AssertionRequest assertionRequest = getAuthenticationRequest(loginId);

        try {
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc = PublicKeyCredential.parseAssertionResponseJson(responseJson);

            FinishAssertionOptions options = FinishAssertionOptions.builder()
                    .request(assertionRequest)
                    .response(pkc)
                    .build();

            AssertionResult result = relyingParty.finishAssertion(options);

            if (result.isSuccess()) {
                PasskeyCredential credential = passkeyRepository.findByCredentialId(result.getCredential().getCredentialId().getBytes())
                        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST));

                credential.updateSignCount(result.getSignatureCount());

                return jwtTokenProvider.issueTokenResponse(member.getMemberId(), member.getRole());
            } else throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PASSKEY_REQUEST);
        }
    }
}
