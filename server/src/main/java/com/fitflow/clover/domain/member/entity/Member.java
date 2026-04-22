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


    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 40)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "USER";

    public void updateProfile(String name, String nickname, String email) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }
}
