package com.dodo.smartsafereturn.auth.controller;

import com.dodo.smartsafereturn.auth.dto.LoginDto;
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
import org.springframework.web.bind.annotation.RequestBody;
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


    /**
     * 아래는 swagger 명세를 위해 만든 더미 데이터 이므로 실제로 코드와 연관없음
     *
     */
    @Operation(
            summary = "로그인 API (문서용이므로 실제 URL 은 \"-doc\"을 제외하고 요청)",
            description = "사용자 ID와 비밀번호로 로그인하고 인증 토큰을 반환합니다. 실제 엔드포인트는 `/api/auth/login` 이며 필터에서 처리됩니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로그인 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "로그인 요청 예시",
                                            summary = "사용자 ID와 비밀번호",
                                            value = """
                                                    {
                                                      "id": "user123",
                                                      "password": "userPassword123!"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공 - Authorization 헤더와 refresh 헤더에 토큰이 반환됩니다.",
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
                            responseCode = "401",
                            description = "인증 실패 - 아이디 또는 비밀번호가 올바르지 않습니다.",
                            content = @Content
                    )
            }
    )
    @PostMapping("/login-doc")
    public ResponseEntity<Void> loginDoc(@RequestBody LoginDto loginDto) {
        // 실제 구현은 없고 문서화 용도로만 사용
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "로그아웃 API (문서용이므로 실제 URL 은 \"-doc\"을 제외하고 요청)",
            description = "사용자의 Refresh 토큰을 무효화하여 로그아웃 처리합니다. 실제 엔드포인트는 `/api/auth/logout` 이며 필터에서 처리됩니다.",
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
                            description = "로그아웃 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "로그아웃 실패"
                    )
            }
    )
    @PostMapping("/logout-doc")
    public ResponseEntity<Void> logoutDoc() {
        // 실제 구현은 없고 문서화 용도로만 사용
        return ResponseEntity.ok().build();
    }
}
