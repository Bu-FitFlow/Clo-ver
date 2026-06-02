package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.response.CommentResponse;
import com.fitflow.clover.domain.community.entity.Comment;
import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.repository.CommentRepository;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final BlockService blockService;
    private final NotificationService notificationService;

    @Transactional
    public Long createComment(Long communityId, Long memberId, String content) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (blockService.isBlocked(memberId, community.getMemberId())) {
            throw new IllegalStateException("차단한 사용자의 게시글에는 댓글을 달 수 없습니다.");
        }

        Comment comment = Comment.builder()
                .communityId(communityId)
                .memberId(memberId)
                .content(content)
                .parentId(null)
                .isDeleted(0)
                .build();

        commentRepository.save(comment);
        community.increaseCommentCount();

        Member sender = memberRepository.findById(memberId).orElse(null);
        if (sender != null) {
            notificationService.notifyComment(community.getMemberId(), memberId, communityId, sender.getNickname());
        }

        return comment.getCommentId();
    }

    @Transactional
    public Long createReply(Long communityId, Long parentId, Long memberId, String content) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (blockService.isBlocked(memberId, community.getMemberId())) {
            throw new IllegalStateException("차단한 사용자의 게시글에는 댓글을 달 수 없습니다.");
        }

        Comment reply = Comment.builder()
                .communityId(communityId)
                .memberId(memberId)
                .content(content)
                .parentId(parentId)
                .isDeleted(0)
                .build();

        commentRepository.save(reply);
        community.increaseCommentCount();

        Comment parentComment = commentRepository.findById(parentId).orElse(null);
        Member sender = memberRepository.findById(memberId).orElse(null);
        if (parentComment != null && sender != null) {
            notificationService.notifyReply(parentComment.getMemberId(), memberId, communityId, sender.getNickname());
        }

        return reply.getCommentId();
    }

    public List<CommentResponse> getComments(Long communityId, Long currentMemberId) {
        return commentRepository.findByCommunityIdAndParentIdIsNull(communityId).stream()
                .filter(comment -> !blockService.isBlocked(currentMemberId, comment.getMemberId()))
                .map(comment -> {
                    Member writer = memberRepository.findById(comment.getMemberId()).orElse(null);
                    return new CommentResponse(
                            comment.getCommentId(),
                            comment.getCommunityId(),
                            comment.getContent(),
                            comment.getMemberId(),
                            writer != null ? writer.getNickname() : "알 수 없음",
                            null,
                            comment.getMemberId().equals(currentMemberId),
                            comment.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMemberId().equals(memberId)) {
            throw new IllegalStateException("본인 댓글만 삭제할 수 있습니다.");
        }

        Community community = communityRepository.findById(comment.getCommunityId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        community.decreaseCommentCount();
        commentRepository.delete(comment);
    }
}