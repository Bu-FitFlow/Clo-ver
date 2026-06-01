package com.fitflow.clover.domain.member.repository;

import com.fitflow.clover.domain.member.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchMembers(String keyword, String role);
}
