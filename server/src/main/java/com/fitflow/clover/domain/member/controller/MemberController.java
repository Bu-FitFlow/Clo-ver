package com.fitflow.clover.domain.member.controller;

import com.fitflow.clover.domain.member.dto.MemberResponse;
import com.fitflow.clover.domain.member.dto.SignUpRequest;
import com.fitflow.clover.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String memberId = userDetails.getUsername();
        return ResponseEntity.ok("환영합니다! 당신의 회원 식별 번호는 " + memberId + " 입니다.");
    }
}
