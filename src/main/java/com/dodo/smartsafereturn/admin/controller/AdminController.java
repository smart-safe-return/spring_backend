package com.dodo.smartsafereturn.admin.controller;

import com.dodo.smartsafereturn.admin.dto.AdminCreateDto;
import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.admin.dto.AdminUpdateDto;
import com.dodo.smartsafereturn.admin.service.AdminService;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 API", description = "관리자 API CRUD 구현")
public class AdminController {

    private final AdminService adminService;

    // 관리자 등록
    @Operation(
            summary = "관리자 등록",
            description = "json 형식으로 MemberJoinDto 를 받아와서 회원 가입 요청 진행",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 관리자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "관리자 등록 요청 Dto 예시",
                                            summary = "관리자 등록 요청을 보낼 때 body 에 보내야 할 것들 입니다",
                                            value = "{\n" +
                                                    "  \"id\": \"admin\",\n" +
                                                    "  \"password\": \"!test1234\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "관리자 등록 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "관리자 등록 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth") // 관리자는 개발측에서 제공한 마스터 계정 or 관리자 계정 로그인 후, 생성하도록 함
    @PostMapping("")
    public ResponseEntity<?> createAdmin(@Validated @RequestBody AdminCreateDto dto) {
        adminService.createAdmin(dto);
        return ResponseEntity.ok().build();
    }

    // 관리자 수정 (비밀번호)
    @Operation(
            summary = "관리자 수정 (비밀번호 변경)",
            description = "json 형식으로 AdminUpdateDto 를 받아와서 회원 수정 요청 진행",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 관리자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "관리자 수정 요청 Dto 예시",
                                            summary = "관리자 수정 요청을 보낼 때 body 에 보내야 할 것들 입니다",
                                            value = "{\n" +
                                                    "  \"admin_number\": \"1\",\n" +
                                                    "  \"password\": \"!test4321\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "관리자 수정 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "관리자 수정 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{adminNumber}")
    public ResponseEntity<?> updateAdmin(@Validated @RequestBody AdminUpdateDto dto) {
        adminService.updateAdmin(dto);
        return ResponseEntity.ok().build();
    }

    // 관리자 삭제 (delete flag 변경)
    @Operation(
            summary = "관리자 탈퇴",
            description = "path variable 로 admin_number 관리자 PK 값을 가져와서 탈퇴 처리 (is_delete 플래그 변경)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "관리자 탈퇴 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "관리자 탈퇴 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{adminNumber}")
    public ResponseEntity<?> deleteAdmin(
            @Parameter(description = "관리자 PK (admin_number)", required = true, example = "1")
            @PathVariable Long adminNumber) {
        adminService.deleteAdmin(adminNumber);
        return ResponseEntity.ok().build();
    }

    // 관리자 정보 불러오기 (한건)
    @Operation(
            summary = "관리자 한 건 조회",
            description = "admin_number PK 값으로 관리자 한 명의 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "관리자 한 건 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdminResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "관리자 정보 응답 예시",
                                                    summary = "성공 시, 관리자 정보 응답 dto JSON 정보",
                                                    value = "{\n" +
                                                            "  \"admin_number\": \"1\",\n" +
                                                            "  \"id\": \"!test4321\",\n" +
                                                            "  \"create_date\": \"2025-04-03T02:10:01.759Z\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "데이터를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{adminNumber}")
    public ResponseEntity<?> getAdmin(
            @Parameter(description = "관리자 PK (admin_number)", required = true, example = "1")
            @PathVariable Long adminNumber) {
        return ResponseEntity.ok().body(adminService.getAdmin(adminNumber));
    }

    // 관리자 정보 불러오기 (리스트)
    @Operation(
            summary = "관리자 리스트 조회",
            description = "관리자 전체의 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "관리자 리스트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdminResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "관리자 정보 응답 예시",
                                                    summary = "성공 시, 관리자 정보 리스트 응답 dto JSON 정보",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"admin_number\": \"1\",\n" +
                                                            "    \"id\": \"!test4321\",\n" +
                                                            "    \"create_date\": \"2025-04-03T02:10:01.759Z\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"admin_number\": \"2\",\n" +
                                                            "    \"id\": \"admin2\",\n" +
                                                            "    \"create_date\": \"2025-04-03T05:20:15.123Z\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "데이터를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.ok().body(adminService.getAdmins());
    }
}
