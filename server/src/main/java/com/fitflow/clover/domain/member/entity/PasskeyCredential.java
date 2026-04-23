package com.fitflow.clover.domain.member.entity;

import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasskeyCredential extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passkeyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Lob
    @Column(nullable = false)
    private byte[] credentialId;

    @Lob
    @Column(nullable = false)
    private byte[] publicKey;

    @Column(nullable = false)
    private Long signCount;

    @Lob
    @Column(nullable = false)
    private byte[] userHandle;

    @Builder
    public PasskeyCredential(Member member, byte[] credentialId, byte[] publicKey, Long signCount, byte[] userHandle) {
        this.member = member;
        this.credentialId = credentialId;
        this.publicKey = publicKey;
        this.signCount = signCount;
        this.userHandle = userHandle;
    }

    public void updateSignCount(Long newSignCount) {
        this.signCount = newSignCount;
    }
}
