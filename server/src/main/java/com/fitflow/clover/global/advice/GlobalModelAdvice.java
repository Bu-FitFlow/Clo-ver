package com.fitflow.clover.global.advice;

import com.fitflow.clover.domain.admin.entity.Admin;
import com.fitflow.clover.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class GlobalModelAdvice {
    private final AdminRepository adminRepository;

    @ModelAttribute("adminName")
    public String getAdminName(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "관리자";
        }

        String loginId = authentication.getName();

        return adminRepository.findByLoginId(loginId)
                .map(Admin::getName)
                .orElse(loginId);
    }
}
