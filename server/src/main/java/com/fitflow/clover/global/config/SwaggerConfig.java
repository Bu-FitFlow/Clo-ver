package com.fitflow.clover.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Clo-ver API 명세서")
                .description("의류 전문 중고 거래 플랫폼 Clo-ver의 백엔드 API 문서입니다.\n\n" +
                        "### 🔐 인증 방식 안내\n" +
                        "- 회원가입/로그인을 제외한 API는 로그인 GET API를 통해 발급받은 **Access Token**이 필요합니다.\n\n" +
                        "- **이곳(Swagger)에서 테스트할 때:** 우측 상단의 `Authorize` 버튼을 누르고 발급받은 토큰을 입력하세요.\n\n" +
                        "- **실제 앱(Android) 연동 시:** HTTP 요청 헤더에 `Authorization: Bearer {발급받은 토큰}` 형태로 넣어주세요."
                )
                .version("1.0.0");

        String securitySchemeName = "bearerAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);
        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}