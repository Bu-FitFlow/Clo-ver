package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.notification.dto.NotificationCreateRequest;
import com.fitflow.clover.domain.notification.service.NotificationService;
import com.fitflow.clover.global.infra.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityCommonService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final NotificationService notificationService;

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

        Member sender = memberRepository.findById(memberId).orElse(null);

        if (sender != null && !community.getMemberId().equals(memberId)) {
            notificationService.createNotification(new NotificationCreateRequest(
                    community.getMemberId(),
                    memberId,
                    "LIKE",
                    sender.getNickname() + "님이 회원님의 게시글을 좋아합니다.",
                    communityId
            ));
        }

        return community.getWishlistCount();
    }
}