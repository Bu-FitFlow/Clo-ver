package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.entity.Comment;
import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.repository.CommentRepository;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;

    @Operation(summary = "댓글 등록")
    @Transactional
    public Long createComment(Long communityId, Long memberId, String content) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment comment = Comment.builder()
                .communityId(communityId)
                .memberId(memberId)
                .content(content)
                .parentId(null)
                .isDeleted(0)
                .build();

        commentRepository.save(comment);
        community.increaseCommentCount();

        return comment.getCommentId();
    }

    @Operation(summary = "대댓글 등록")
    @Transactional
    public Long createReply(Long communityId, Long parentId, Long memberId, String content) {
        communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        Comment reply = Comment.builder()
                .communityId(communityId)
                .memberId(memberId)
                .content(content)
                .parentId(parentId)
                .isDeleted(0)
                .build();

        commentRepository.save(reply);

        return reply.getCommentId();
    }

    public List<Comment> getComments(Long communityId) {
        return commentRepository.findByCommunityIdAndParentIdIsNull(communityId);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMemberId().equals(memberId)) {
            throw new IllegalStateException("본인 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}