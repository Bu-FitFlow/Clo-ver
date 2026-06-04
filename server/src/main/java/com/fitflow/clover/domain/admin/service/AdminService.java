package com.fitflow.clover.domain.admin.service;

import com.fitflow.clover.domain.admin.dto.request.AdminSignUpRequest;
import com.fitflow.clover.domain.admin.entity.Admin;
import com.fitflow.clover.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(AdminSignUpRequest request) {
        if (adminRepository.findByLoginId(request.loginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 관리자 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Admin admin = Admin.builder()
                .loginId(request.loginId())
                .password(encodedPassword)
                .name(request.name())
                .email(request.email())
                .role("ROLE_ADMIN")
                .build();

        adminRepository.save(admin);
    }

    public List<Admin> getPendingAdmins() {
        return adminRepository.findByIsApproved(false);
    }

    @Transactional
    public void approveAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
        admin.approve();
    }

    @Transactional
    public void rejectAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        if (admin.isApproved()) {
            throw new IllegalStateException("이미 승인된 계정은 반려(삭제)할 수 없습니다.");
        }

        adminRepository.delete(admin);
    }
}
