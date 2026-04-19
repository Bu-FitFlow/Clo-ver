package com.fitflow.clover.member;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String signup(SignupRequest request) {

        Member member = new Member();
        member.setMembership(UUID.randomUUID().toString());
        member.setId(request.getId());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setNickname(request.getNickname());
        member.setEmail(request.getEmail());
        member.setSex(request.getSex());
        member.setRole(1);
        member.setRegDate(LocalDateTime.now());

        memberRepository.save(member);

        return "회원가입 저장 성공";
    }
}