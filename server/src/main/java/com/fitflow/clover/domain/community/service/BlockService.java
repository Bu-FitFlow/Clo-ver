package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.global.infra.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    private static final long TEN_YEARS_MILLIS = 10L * 365 * 24 * 60 * 60 * 1000;

    private String blockKey(Long blockerId, Long blockedId) {
        return "BLOCK:" + blockerId + ":" + blockedId;
    }

    public void blockByNickname(Long blockerId, String nickname) {
        Member blocked = memberRepository.findAll().stream()
                .filter(m -> m.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다."));

        if (blockerId.equals(blocked.getMemberId())) {
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다.");
        }

        redisUtil.setDataExpire(blockKey(blockerId, blocked.getMemberId()), "blocked", TEN_YEARS_MILLIS);
    }

    public void unblockByNickname(Long blockerId, String nickname) {
        Member blocked = memberRepository.findAll().stream()
                .filter(m -> m.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다."));

        redisUtil.deleteData(blockKey(blockerId, blocked.getMemberId()));
    }

    public boolean isBlocked(Long blockerId, Long blockedId) {
        return redisUtil.getData(blockKey(blockerId, blockedId)) != null;
    }
}
