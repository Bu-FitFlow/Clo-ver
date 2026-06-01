package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.response.CommunitySearchResponse;
import com.fitflow.clover.domain.community.service.CommunitySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "커뮤니티 검색", description = "커뮤니티 통합 검색 API")
@RestController
@RequestMapping("/api/community/search")
@RequiredArgsConstructor
public class CommunitySearchController {

    private final CommunitySearchService communitySearchService;

    @Operation(summary = "커뮤니티 통합 검색 (제목·내용)")
    @GetMapping
    public ResponseEntity<List<CommunitySearchResponse>> search(
            @RequestParam String keyword,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(communitySearchService.search(keyword, memberId));
    }
}