package com.fitflow.clover.controller;

import com.fitflow.clover.member.MemberService;
import com.fitflow.clover.member.SignupRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final MemberService memberService;

    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String user() {
        return "user 기본 페이지";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "회원가입 페이지 연결 성공";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return memberService.signup(request);
    }

    @GetMapping("/signup-test")
    public String signupTest() {
        SignupRequest request = new SignupRequest();
        request.setId("test123");
        request.setPassword("1234");
        request.setName("홍길동");
        request.setNickname("길동이");
        request.setEmail("test@test.com");
        request.setSex("남");

        return memberService.signup(request);
    }
}