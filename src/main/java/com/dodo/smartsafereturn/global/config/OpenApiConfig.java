package com.dodo.smartsafereturn.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        // Info 객체: API의 기본 메타데이터(제목, 버전, 설명 등)를 설정합니다.
        Info info = new Info()
                .title("안전 귀가 API Documentation")
                .version("v1.0")
                .description("안전 귀가 프로젝트의 API 문서입니다.");

        // SecurityScheme: API에 필요한 인증 방식을 정의합니다
        // JWT 토큰을 사용하는 경우: JWT 보안 스키마 설정
        String securitySchemeName = "bearerAuth";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // SecurityRequirement: 전체 API에 적용할 기본 보안 요구사항을 정의합니다.
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme)) // Components: 재사용 가능한 OpenAPI 구성 요소(보안 스키마, 스키마 등)를 정의합니다
                .addSecurityItem(securityRequirement);
    }
}
