package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.service.BlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "차단", description = "회원 차단 관리 API")
@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @Operation(summary = "회원 차단 (닉네임으로)")
    @PostMapping("/{nickname}")
    public ResponseEntity<Void> block(
            @PathVariable String nickname,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        blockService.blockByNickname(memberId, nickname);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "차단 해제 (닉네임으로)")
    @DeleteMapping("/{nickname}")
    public ResponseEntity<Void> unblock(
            @PathVariable String nickname,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        blockService.unblockByNickname(memberId, nickname);
        return ResponseEntity.ok().build();
    }
}