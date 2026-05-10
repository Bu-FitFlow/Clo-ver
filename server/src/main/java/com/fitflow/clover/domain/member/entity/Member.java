package com.fitflow.clover.domain.member.entity;

import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(name = "totp_secret", length = 64)
    private String totpSecret;

    @Builder.Default
    @Column(name = "is_totp_enabled", nullable = false, columnDefinition = "TINYINT")
    private boolean totpEnabled = false;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 40)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isEmailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "USER";

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "member_rank", nullable = false)
    private MemberRank rank = MemberRank.SEED;

    @Builder.Default
    @Column(name = "is_deleted", columnDefinition = "TINYINT")
    private boolean isDeleted = false;

    public void verifyEmail() {
        this.isEmailVerified = true;
    }

    public void updateTotpSecret(String secret) {
        this.totpSecret = secret;
    }

    public void enableTotp() {
        this.totpEnabled = true;
    }

    public void disableTotp() {
        this.totpEnabled = false;
        this.totpSecret = null;
    }

    public void updateProfile(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void withdraw() {
        this.isDeleted = true;
        this.nickname = "탈퇴한 회원";
        this.email = "withdrawn@" + this.memberId;
    }
}
