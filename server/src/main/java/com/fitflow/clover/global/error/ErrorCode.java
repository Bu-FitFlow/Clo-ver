package com.fitflow.clover.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "적절하지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "인증 정보가 만료되었거나 유효하지 않습니다. 다시 로그인해 주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않거나 만료되었습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),

    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다."),

    INVALID_TOTP_CODE(HttpStatus.BAD_REQUEST, "2차 인증(OTP) 번호가 일치하지 않습니다."),
    TOTP_VERIFICATION_REQUIRED(HttpStatus.PRECONDITION_REQUIRED, "2차 인증(OTP) 번호 입력이 필요합니다."),

    PASSKEY_TIMEOUT(HttpStatus.BAD_REQUEST, "패스키 인증 시간이 초과되었습니다. 다시 시도해 주세요."),
    INVALID_PASSKEY_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 패스키 요청입니다."),
    PASSKEY_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 패스키가 없습니다."),

    UNVERIFIED_EMAIL(HttpStatus.UNAUTHORIZED, "인증되지 않은 이메일입니다. 이메일 인증을 먼저 진행해 주세요."),
    EMAIL_VERIFICATION_PENDING(HttpStatus.FORBIDDEN, "인증 메일이 이미 발송되었습니다. 메일함을 확인해주세요."),
    EMAIL_VERIFICATION_RESENT(HttpStatus.FORBIDDEN, "인증 유효시간이 만료되어 새 인증 메일을 발송했습니다. 메일함을 확인해주세요.");

    private final HttpStatus status;
    private final String message;
}