package com.dodo.smartsafereturn.verification.controller;

import com.dodo.smartsafereturn.verification.dto.*;
import com.dodo.smartsafereturn.verification.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
@Tag(name = "본인 인증 API", description = "SMS 인증을 이용한 본인 인증 기능 + 추후 다양한 인증 확장 가능")
public class VerificationController {

    private final VerificationService verificationService;

    // SMS 비밀 번호 인증 요청
    @Operation(
            summary = "비밀번호 찾기 SMS 인증 요청",
            description = "비밀번호 찾기를 위한 SMS 인증 코드를 사용자의 전화번호로 발송합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 ID와 전화번호 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SMSPasswordRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "비밀번호 찾기 SMS 인증 요청 예시",
                                            summary = "사용자 ID와 전화번호 정보",
                                            value = """
                                                    {
                                                      "member_id": "user123",
                                                      "phone": "01012345678"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SMS 인증 발송 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Long.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "인증 ID 응답 예시",
                                                    summary = "생성된 인증 ID 값",
                                                    value = "1"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/password/sms")
    public ResponseEntity<Long> sendPasswordVerification(@Validated @RequestBody SMSPasswordRequestDto dto) {
        Long verificationId = verificationService.verifyPasswordBySMS(dto);
        // 인증 발송 완료 시 verification_id 반환
        return ResponseEntity.ok(verificationId);
    }

    // SMS 비밀 번호 인증 검증
    @Operation(
            summary = "비밀번호 찾기 SMS 인증 코드 검증",
            description = "비밀번호 찾기를 위해 발송된 SMS 인증 코드를 검증합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "인증 ID와 인증 코드 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidateRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "인증 코드 검증 요청 예시",
                                            summary = "인증 ID와 인증 코드 정보",
                                            value = """
                                                    {
                                                      "verification_id": 1,
                                                      "code": "123456"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "인증 코드 검증 결과",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "인증 성공 응답 예시",
                                                    summary = "간단한 보안을 위한 reset-token 발급",
                                                    value = "JWT 형식의 reset 토큰"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/password/sms/validate")
    public ResponseEntity<String> sendPasswordVerificationValidate(@Validated @RequestBody ValidateRequestDto dto) {
        String isVerified = verificationService.validatePasswordBySMS(dto);
        // 인증 검증 완료 시 resetToken 반환
        return ResponseEntity.ok(isVerified);
    }

    // SMS 아이디 인증 요청
    @Operation(
            summary = "아이디 찾기 SMS 인증 요청",
            description = "아이디 찾기를 위한 SMS 인증 코드를 사용자의 전화번호로 발송합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 전화번호 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SMSMemberIdRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "아이디 찾기 SMS 인증 요청 예시",
                                            summary = "사용자 전화번호 정보",
                                            value = """
                                                    {
                                                      "phone": "01012345678"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SMS 인증 발송 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Long.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "인증 ID 응답 예시",
                                                    summary = "생성된 인증 ID 값",
                                                    value = "1"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "해당 전화번호로 등록된 사용자를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/id/sms")
    public ResponseEntity<Long> sendMemberIdVerification(@Validated @RequestBody SMSMemberIdRequestDto dto) {
        Long verificationId = verificationService.verifyMemberIdBySMS(dto);
        // 인증 발송 완료 시 verification_id 반환
        return ResponseEntity.ok(verificationId);
    }

    // SMS 아이디 인증 검증
    @Operation(
            summary = "아이디 찾기 SMS 인증 코드 검증",
            description = "아이디 찾기를 위해 발송된 SMS 인증 코드를 검증합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "인증 ID와 인증 코드 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidateIdRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "인증 코드 검증 요청 예시",
                                            summary = "인증 ID와 인증 코드 정보",
                                            value = """
                                                    {
                                                      "phone": "01012345678",
                                                      "verification_id": 12345,
                                                      "code": "123456"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "인증 코드 검증 결과",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "인증 성공 응답 예시",
                                                    summary = "인증 성공 시, 회원 아이디 (String) 반환",
                                                    value = "test1234"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "인증 정보를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/id/sms/validate")
    public ResponseEntity<String> sendMemberIdVerificationValidate(@Validated @RequestBody ValidateIdRequestDto dto) {
        String isVerified = verificationService.validateMemberIdBySMS(dto);
        // 인증 검증 완료 시 회원 아이디 반환
        return ResponseEntity.ok(isVerified);
    }

    // SMS 회원 가입 시, 인증 요청
    @Operation(
            summary = "회원가입 SMS 인증 요청",
            description = "회원가입을 위한 SMS 인증 코드를 사용자의 전화번호로 발송합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 전화번호 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SMSSignUpRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "회원가입 SMS 인증 요청 예시",
                                            summary = "사용자 전화번호 정보",
                                            value = """
                                                    {
                                                      "phone": "01012345678"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SMS 인증 발송 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Long.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "인증 ID 응답 예시",
                                                    summary = "생성된 인증 ID 값",
                                                    value = "1"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/signup/sms")
    public ResponseEntity<Long> sendSignUpVerification(@Validated @RequestBody SMSSignUpRequestDto dto) {
        Long verificationId = verificationService.verifySignUpBySMS(dto);
        // 인증 발송 완료 시 verification_id 반환
        return ResponseEntity.ok(verificationId);
    }

    // SMS 회원 가입 시, 인증 검증
    @Operation(
            summary = "회원가입 SMS 인증 코드 검증",
            description = "회원가입을 위해 발송된 SMS 인증 코드를 검증합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "인증 ID와 인증 코드 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidateRequestDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "인증 코드 검증 요청 예시",
                                            summary = "인증 ID와 인증 코드 정보",
                                            value = """
                                                    {
                                                      "verification_id": 1,
                                                      "code": "123456"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "인증 코드 검증 결과",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "인증 성공 응답 예시",
                                                    summary = "인증 성공 여부(true/false)",
                                                    value = "true"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/signup/sms/validate")
    public ResponseEntity<Boolean> sendSignUpVerificationValidate(@Validated @RequestBody ValidateRequestDto dto) {
        Boolean isVerified = verificationService.validateSignUpBySMS(dto);
        // 인증 검증 완료 시 true 반환
        return ResponseEntity.ok(isVerified);
    }

    @Operation(
            summary = "비밀번호 재설정",
            description = "SMS 인증 성공 후, 사용자의 비밀번호를 재설정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 아이디와 새 비밀번호 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PasswordResetDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "비밀번호 재설정 요청 예시",
                                            summary = "사용자 아이디와 새 비밀번호",
                                            value = """
                                                {
                                                  "member_id": "user123",
                                                  "password": "NewPassword123!",
                                                  "token": "JWT 토큰 형식"
                                                }
                                                """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비밀번호 재설정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "재설정 성공 응답 예시",
                                                    summary = "재설정 성공 여부(true/false)",
                                                    value = "true"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PutMapping("/password/reset")
    public ResponseEntity<Boolean> resetPassword(@Validated @RequestBody PasswordResetDto dto) {
        Boolean reset = verificationService.resetPassword(dto);
        return ResponseEntity.ok(reset);
    }
}