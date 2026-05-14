package com.fitflow.clover.domain.member.repository;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.entity.PasskeyCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasskeyRepository extends JpaRepository<PasskeyCredential, Long> {
    List<PasskeyCredential> findAllByMember_MemberId(Long memberId);

    Optional<PasskeyCredential> findByCredentialId(byte[] credentialId);

    List<PasskeyCredential> findAllByUserHandle(byte[] userHandle);

    Optional<PasskeyCredential> findByPasskeyIdAndMember_MemberId(Long id, Long memberId);

    boolean existsByMember(Member member);
}
