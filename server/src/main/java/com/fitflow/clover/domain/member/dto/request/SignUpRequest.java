package com.fitflow.clover.domain.member.dto.request;

import com.fitflow.clover.domain.member.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Size(min = 6, max = 25, message = "아이디는 6~25자리여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]+$", message = "아이디는 영어/숫자/특수문자만 가능합니다.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        String password,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(max = 20)
        String name,

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Size(max = 40)
        String nickname,

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 100)
        String email,

        Gender gender
) {
}
