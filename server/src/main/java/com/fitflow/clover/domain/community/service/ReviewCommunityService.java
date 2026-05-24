package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.request.ReviewPostRequest;
import com.fitflow.clover.domain.community.dto.response.CommunityListResponse;
import com.fitflow.clover.domain.community.dto.response.ReviewDetailResponse;
import com.fitflow.clover.domain.community.entity.BoardType;
import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.repository.CommentRepository;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.global.infra.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final BlockService blockService;

    public List<CommunityListResponse> getReviewList(Long currentMemberId) {
        return communityRepository.findByBoardType(BoardType.REVIEW).stream()
                .filter(c -> !blockService.isBlocked(currentMemberId, c.getMemberId()))
                .map(c -> new CommunityListResponse(
                        c.getCommunityId(),
                        c.getTitle(),
                        c.getMemberId(),
                        c.getViewCount(),
                        c.getCommentCount(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createReviewPost(ReviewPostRequest request, Long memberId) {
        Community community = Community.builder()
                .boardType(BoardType.REVIEW)
                .memberId(memberId)
                .title(request.title())
                .content(request.content())
                .build();
        return communityRepository.save(community).getCommunityId();
    }

    @Transactional
    public ReviewDetailResponse getReviewPost(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 게시글입니다."));

        communityRepository.updateViewCount(communityId);

        Member writer = memberRepository.findById(community.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return new ReviewDetailResponse(
                community.getCommunityId(),
                community.getTitle(),
                community.getContent(),
                List.of(),
                writer.getNickname(),
                null,
                community.getViewCount() + 1,
                community.getCommentCount(),
                community.getWishlistCount(),
                community.getCreatedAt()
        );
    }

    @Transactional
    public void updateReviewPost(Long communityId, ReviewPostRequest request, Long memberId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 게시글입니다."));

        if (!community.getMemberId().equals(memberId)) {
            throw new IllegalStateException("본인 글만 수정할 수 있습니다.");
        }

        community.updateFreePost(request.title(), request.content());
    }

    @Transactional
    public void deleteReviewPost(Long communityId, Long memberId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 게시글입니다."));

        if (!community.getMemberId().equals(memberId)) {
            throw new IllegalStateException("본인 글만 삭제할 수 있습니다.");
        }

        communityRepository.delete(community);
    }

    @Transactional
    public int toggleLike(Long communityId, Long memberId) {
        String redisKey = "LIKE:" + memberId + ":" + communityId;

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (redisUtil.getData(redisKey) != null) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        community.increaseLikeCount();
        redisUtil.setDataExpire(redisKey, "liked", 24 * 60 * 60 * 1000L);

        return community.getWishlistCount();
    }
}