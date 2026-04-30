package com.fitflow.clover.domain.member.dto.response;

import com.fitflow.clover.domain.member.entity.Gender;
import com.fitflow.clover.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long memberId;
    private String loginId;
    private String name;
    private String nickname;
    private String email;
    private Gender gender;
    private String role;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .gender(member.getGender())
                .role(member.getRole())
                .build();
    }
}
