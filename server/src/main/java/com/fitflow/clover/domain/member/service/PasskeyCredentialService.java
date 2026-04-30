package com.fitflow.clover.domain.member.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.entity.PasskeyCredential;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.member.repository.PasskeyRepository;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasskeyCredentialService implements CredentialRepository {
    private final PasskeyRepository passkeyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        Optional<Member> memberOpt = memberRepository.findByLoginId(username);
        if (memberOpt.isEmpty()) return Set.of();

        List<PasskeyCredential> credentials = passkeyRepository.findAllByMember_MemberId(memberOpt.get().getMemberId());

        return credentials.stream()
                .map(cred -> PublicKeyCredentialDescriptor.builder()
                        .id(new ByteArray(cred.getCredentialId()))
                        .build())
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return memberRepository.findByLoginId(username)
                .flatMap(member -> passkeyRepository.findAllByMember_MemberId(member.getMemberId()).stream().findFirst())
                .map(cred -> new ByteArray(cred.getUserHandle()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return passkeyRepository.findAllByUserHandle(userHandle.getBytes()).stream()
                .findFirst()
                .map(cred -> cred.getMember().getLoginId());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        Optional<PasskeyCredential> credOpt = passkeyRepository.findByCredentialId(credentialId.getBytes());
        if (credOpt.isEmpty()) return Optional.empty();

        PasskeyCredential cred = credOpt.get();
        if (!Arrays.equals(cred.getUserHandle(), userHandle.getBytes())) return Optional.empty();

        return Optional.of(RegisteredCredential.builder()
                .credentialId(new ByteArray(cred.getCredentialId()))
                .userHandle(new ByteArray(cred.getUserHandle()))
                .publicKeyCose(new ByteArray(cred.getPublicKey()))
                .signatureCount(cred.getSignCount())
                .build());
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        Optional<PasskeyCredential> credOpt = passkeyRepository.findByCredentialId(credentialId.getBytes());
        if (credOpt.isEmpty()) return Set.of();

        PasskeyCredential cred = credOpt.get();
        RegisteredCredential rc = RegisteredCredential.builder()
                .credentialId(new ByteArray(cred.getCredentialId()))
                .userHandle(new ByteArray(cred.getUserHandle()))
                .publicKeyCose(new ByteArray(cred.getPublicKey()))
                .signatureCount(cred.getSignCount())
                .build();

        return Set.of(rc);
    }
}
