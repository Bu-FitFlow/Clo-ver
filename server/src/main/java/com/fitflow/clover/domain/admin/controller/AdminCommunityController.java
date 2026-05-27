package com.fitflow.clover.domain.admin.controller;

import com.fitflow.clover.domain.admin.dto.response.AdminCommunityResponse;
import com.fitflow.clover.domain.admin.service.AdminCommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "관리자 커뮤니티 글 관리 (Web)", description = "커뮤니티 글 목록 조회 및 상태 변경(숨김, 삭제)을 처리합니다.")
@Controller
@RequestMapping("/communities")
@RequiredArgsConstructor
public class AdminCommunityController {
    private final AdminCommunityService adminCommunityService;

    @Operation(summary = "커뮤니티 글 목록 페이지 조회")
    @GetMapping
    public String communityList(
            @PageableDefault(size = 10) Pageable pageable,
            Model model
    ) {
        Page<AdminCommunityResponse> pagedCommunities = adminCommunityService.getPagedAdminCommunities(pageable);

        model.addAttribute("communities", pagedCommunities.getContent());
        model.addAttribute("page", pagedCommunities);
        return "admin/community-list";
    }

    @Operation(summary = "게시글 숨김(블라인드) 처리")
    @PostMapping("/{communityId}/hide")
    public String hideCommunityPost(@PathVariable Long communityId) {
        adminCommunityService.changePostStatus(communityId, "HIDDEN");
        return "redirect:/communities";
    }
}
