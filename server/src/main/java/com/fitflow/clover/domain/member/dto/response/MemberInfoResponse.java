package com.fitflow.clover.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {
    private String loginId;
    private String name;
    private String nickname;
    private String email;
    private boolean totpEnabled;
    private boolean hasPasskey;
}
