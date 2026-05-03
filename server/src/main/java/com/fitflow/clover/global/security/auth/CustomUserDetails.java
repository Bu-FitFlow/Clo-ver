package com.fitflow.clover.global.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final Long memberId;
    private final String password;
    private final String role;

    public CustomUserDetails(Long memberId, String password, String role) {
        this.memberId = memberId;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.memberId);
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
