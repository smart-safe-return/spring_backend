package com.dodo.smartsafereturn.auth.controller;

import com.dodo.smartsafereturn.auth.dto.RefreshValidationResultDto;
import com.dodo.smartsafereturn.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT Refresh Token 관련 인증 기능
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증/인가 API", description = "Jwt accessToken 만료 시, refreshToken 기반 재발급 요청 API 구현")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "토큰 재발급",
            description = "Refresh 토큰을 이용하여 Access 토큰과 Refresh 토큰을 재발급합니다",
            parameters = {
                    @Parameter(
                            name = "refresh",
                            description = "Refresh 토큰 (Bearer 포함)",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰 재발급 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "성공 응답",
                                                    summary = "토큰 재발급 성공 시 응답",
                                                    value = "{\n" +
                                                            "  \"body\": \"access token reissued successfully\",\n" +
                                                            "  \"status\": \"OK\"\n" +
                                                            "}"
                                            )
                                    }
                            ),
                            headers = {
                                    @io.swagger.v3.oas.annotations.headers.Header(
                                            name = "Authorization",
                                            description = "새로 발급된 Access 토큰",
                                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                                    ),
                                    @io.swagger.v3.oas.annotations.headers.Header(
                                            name = "refresh",
                                            description = "새로 발급된 Refresh 토큰",
                                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "토큰 재발급 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "누락된 토큰",
                                                    summary = "Refresh 토큰이 요청에 없음",
                                                    value = "{\n" +
                                                            "  \"body\": \"refresh token is null\",\n" +
                                                            "  \"status\": \"BAD_REQUEST\"\n" +
                                                            "}"
                                            ),
                                            @ExampleObject(
                                                    name = "만료된 토큰",
                                                    summary = "만료된 Refresh 토큰 사용",
                                                    value = "{\n" +
                                                            "  \"body\": \"refresh token is expired\",\n" +
                                                            "  \"status\": \"BAD_REQUEST\"\n" +
                                                            "}"
                                            ),
                                            @ExampleObject(
                                                    name = "유효하지 않은 토큰 타입",
                                                    summary = "Refresh 타입이 아닌 토큰 사용",
                                                    value = "{\n" +
                                                            "  \"body\": \"invalid refresh token\",\n" +
                                                            "  \"status\": \"BAD_REQUEST\"\n" +
                                                            "}"
                                            ),
                                            @ExampleObject(
                                                    name = "저장소에 없는 토큰",
                                                    summary = "토큰 저장소에 등록되지 않은 Refresh 토큰",
                                                    value = "{\n" +
                                                            "  \"body\": \"invalid refresh token\",\n" +
                                                            "  \"status\": \"BAD_REQUEST\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // refresh 헤더에서 리프레시 토큰 가져오기
        String refreshToken = request.getHeader("refresh");
        // 재발급 로직
        RefreshValidationResultDto result = authService.reissue(refreshToken, response);

        return new ResponseEntity<>(result.getBody(), result.getStatus());

    }
}
