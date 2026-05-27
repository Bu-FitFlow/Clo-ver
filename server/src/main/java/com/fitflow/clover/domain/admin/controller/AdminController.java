package com.fitflow.clover.domain.admin.controller;

import com.fitflow.clover.domain.admin.dto.request.AdminSignUpRequest;
import com.fitflow.clover.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping({"", "/"})
    public String adminHome(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("loginId", principal.getName());
        }
        return "admin/home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "admin/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute AdminSignUpRequest request) {
        adminService.signUp(request);
        return "redirect:/login";
    }
}
