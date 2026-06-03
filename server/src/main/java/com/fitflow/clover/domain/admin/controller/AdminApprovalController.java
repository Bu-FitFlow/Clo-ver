package com.fitflow.clover.domain.admin.controller;

import com.fitflow.clover.domain.admin.entity.Admin;
import com.fitflow.clover.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/approvals")
@RequiredArgsConstructor
public class AdminApprovalController {
    private final AdminService adminService;

    @GetMapping
    public String getPendingAdmins(Model model) {
        List<Admin> pendingAdmins = adminService.getPendingAdmins();
        model.addAttribute("approvals", pendingAdmins);

        return "admin/approval-list";
    }

    @PostMapping("/{adminId}/approve")
    public String approveAdmin(@PathVariable Long adminId) {
        adminService.approveAdmin(adminId);
        return "redirect:/approvals";
    }

    @PostMapping("/{adminId}/reject")
    public String rejectAdmin(@PathVariable Long adminId) {
        adminService.rejectAdmin(adminId);
        return "redirect:/approvals";
    }
}
