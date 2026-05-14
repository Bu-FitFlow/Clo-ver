package com.fitflow.clover.domain.member.dto.response;

import com.fitflow.clover.domain.member.entity.Gender;
import com.fitflow.clover.domain.member.entity.Member;

public record MemberResponse(
        Long memberId,
        String loginId,
        String name,
        String nickname,
        String email,
        Gender gender,
        String role
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getLoginId(),
                member.getName(),
                member.getNickname(),
                member.getEmail(),
                member.getGender(),
                member.getRole()
        );
    }
}
