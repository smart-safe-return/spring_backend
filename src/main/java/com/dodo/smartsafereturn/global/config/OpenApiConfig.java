package com.dodo.smartsafereturn.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        // Info 객체: API의 기본 메타데이터(제목, 버전, 설명 등)를 설정합니다.
        Info info = new Info()
                .title("안전 귀가 API Documentation")
                .version("v1.0")
                .contact(new Contact()
                        .name("이규찬")
                        .email("hoo788@gmail.com")
                        .url("https://github.com/smart-safe-return/spring_backend"))
                .description("안전 귀가 프로젝트의 API 문서입니다.");

        // SecurityScheme: API에 필요한 인증 방식을 정의합니다
        // JWT 토큰을 사용하는 경우: JWT 보안 스키마 설정
        String securitySchemeName = "bearerAuth";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(info)
                .servers(Arrays.asList(
                        new Server().url("https://smart-safe-return-backend-88013499747.asia-northeast2.run.app").description("운영 서버"),
                        new Server().url("http://localhost:8080").description("개발 서버")))
                .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme));
                // Components: 재사용 가능한 OpenAPI 구성 요소(보안 스키마, 스키마 등)를 정의합니다
    }
}
