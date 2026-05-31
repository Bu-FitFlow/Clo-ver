package com.fitflow.clover.domain.admin.service;

import com.fitflow.clover.domain.admin.entity.Admin;
import com.fitflow.clover.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("관리자 계정을 찾을 수 없습니다."));

        if (!admin.isApproved()) {
            throw new DisabledException("관리자의 승인이 필요한 계정입니다.");
        }

        return User.builder()
                .username(admin.getLoginId())
                .password(admin.getPassword())
                .roles(admin.getRole().replace("ROLE_", ""))
                .build();
    }
}
