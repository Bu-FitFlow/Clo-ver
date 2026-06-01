package com.fitflow.clover.global.config;

import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            System.out.println("👀 [웹소켓 연결 시도] 전달받은 헤더: " + authHeader);

            if (authHeader == null || authHeader.trim().isEmpty()) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            String token = resolveToken(authHeader);

            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                accessor.setUser(authentication);

                System.out.println("✅ [웹소켓 인증 성공] 접속한 유저 ID: " + authentication.getName());
            } else {
                System.out.println("❌ [웹소켓 인증 실패] 토큰이 만료되었거나 유효하지 않습니다.");
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
        }
        return message;
    }

    private String resolveToken(String authHeader) {
        if (StringUtils.hasText(authHeader)) {
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return authHeader.trim();
        }
        return null;
    }
}