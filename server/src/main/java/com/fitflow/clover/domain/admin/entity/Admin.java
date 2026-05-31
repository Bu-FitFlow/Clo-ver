package com.fitflow.clover.domain.admin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role = "ROLE_ADMIN";

    @Column(nullable = false)
    private boolean isApproved = false;

    // 추후 TOTP 연동 시 주석 해제하여 사용
    // private String totpSecret;
    // private boolean isTotpEnabled = false;

    @Builder
    public Admin(String loginId, String password, String email, String name, String role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        if (role != null) {
            this.role = role;
        }
    }

    public void approve() {
        this.isApproved = true;
    }
}
