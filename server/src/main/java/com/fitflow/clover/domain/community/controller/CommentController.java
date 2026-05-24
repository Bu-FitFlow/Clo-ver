package com.fitflow.clover.domain.community.controller;

import com.fitflow.clover.domain.community.dto.request.CommentPostRequest;
import com.fitflow.clover.domain.community.dto.response.CommentResponse;
import com.fitflow.clover.domain.community.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글", description = "댓글 통합 관리 API")
@RestController
@RequestMapping("/api/community/{communityId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회")
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long communityId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(commentService.getComments(communityId, memberId));
    }

    @Operation(summary = "댓글 등록")
    @PostMapping
    public ResponseEntity<Long> createComment(
            @PathVariable Long communityId,
            @RequestBody @Valid CommentPostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(commentService.createComment(communityId, memberId, request.content()));
    }

    @Operation(summary = "대댓글 등록")
    @PostMapping("/{parentId}/reply")
    public ResponseEntity<Long> createReply(
            @PathVariable Long communityId,
            @PathVariable Long parentId,
            @RequestBody @Valid CommentPostRequest request,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(commentService.createReply(communityId, parentId, memberId, request.content()));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long communityId,
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "대댓글 삭제")
    @DeleteMapping("/{parentId}/reply/{commentId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long communityId,
            @PathVariable Long parentId,
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        Long memberId = Long.parseLong(authentication.getName());
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok().build();
    }
}