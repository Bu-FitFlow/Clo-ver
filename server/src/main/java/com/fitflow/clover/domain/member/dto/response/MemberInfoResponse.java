package com.fitflow.clover.domain.member.dto.response;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.entity.MemberRank;

public record MemberInfoResponse(
        String loginId,
        String name,
        String nickname,
        String email,
        MemberRank rank,
        boolean totpEnabled,
        boolean hasPasskey
) {
    public static MemberInfoResponse from(Member member, boolean hasPasskey) {
        return new MemberInfoResponse(
                member.getLoginId(),
                member.getName(),
                member.getNickname(),
                member.getEmail(),
                member.getRank(),
                member.isTotpEnabled(),
                hasPasskey
        );
    }
}
