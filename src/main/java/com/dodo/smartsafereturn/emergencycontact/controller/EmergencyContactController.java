package com.dodo.smartsafereturn.emergencycontact.controller;

import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactCreateDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactResponseDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactUpdateDto;
import com.dodo.smartsafereturn.emergencycontact.service.EmergencyContactService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/emergency-contact") // 케밥케이스 (보통 권장)
@RequiredArgsConstructor
@Tag(name = "SOS 비상연락망 API", description = "비상연락망 API CRUD 구현")
public class EmergencyContactController {

    private final EmergencyContactService service;

    // sos 비상연락망 등록
    @Operation(
            summary = "SOS 비상연락망 등록",
            description = "새로운 SOS 비상연락망을 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 비상연락망 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmergencyContactCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "비상연락망 등록 요청 예시",
                                            summary = "비상연락망 등록 요청 시 필요한 데이터",
                                            value = "{\n" +
                                                    "  \"member_number\": 1,\n" +
                                                    "  \"name\": \"홍길동\",\n" +
                                                    "  \"phone\": \"01012345678\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비상연락망 등록 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("")
    public ResponseEntity<?> createEmergencyContact(@Validated @RequestBody EmergencyContactCreateDto dto) {
        service.create(dto);
        return ResponseEntity.ok().build();
    }

    // sos 비상연락망 수정
    @Operation(
            summary = "SOS 비상연락망 수정",
            description = "기존 SOS 비상연락망 정보를 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 비상연락망 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmergencyContactUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "비상연락망 수정 요청 예시",
                                            summary = "비상연락망 수정 요청 시 필요한 데이터",
                                            value = "{\n" +
                                                    "  \"emergency_contact_id\": 1,\n" +
                                                    "  \"name\": \"김철수\",\n" +
                                                    "  \"phone\": \"01098765432\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비상연락망 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmergencyContact(@Validated @RequestBody EmergencyContactUpdateDto dto, @PathVariable String id) {
        service.update(dto);
        return ResponseEntity.ok().build();
    }

    // sos 비상연락망 삭제
    @Operation(
            summary = "SOS 비상연락망 삭제",
            description = "SOS 비상연락망을 삭제합니다.",
            parameters = {
                    @Parameter(name = "id", description = "삭제할 비상연락망 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비상연락망 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "비상연락망을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmergencyContact(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // sos 비상연락망 조회 (마이 페이지 - 회원이 등록한 sos 비상 연락망 조회)
    @Operation(
            summary = "회원별 SOS 비상연락망 목록 조회",
            description = "특정 회원이 등록한 모든 SOS 비상연락망을 조회합니다.",
            parameters = {
                    @Parameter(name = "memberNumber", description = "회원 번호", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비상연락망 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmergencyContactResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "비상연락망 목록 응답 예시",
                                                    summary = "회원의 비상연락망 목록",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"emergency_contact_id\": 1,\n" +
                                                            "    \"name\": \"홍길동\",\n" +
                                                            "    \"phone\": \"01012345678\",\n" +
                                                            "    \"create_date\": \"2025-04-03T10:15:30.123Z\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"emergency_contact_id\": 2,\n" +
                                                            "    \"name\": \"김철수\",\n" +
                                                            "    \"phone\": \"01098765432\",\n" +
                                                            "    \"create_date\": \"2025-04-03T11:20:45.789Z\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<EmergencyContactResponseDto>> getEmergencyContact(@PathVariable Long memberNumber) {

        return new ResponseEntity<>(service.getMemberContacts(memberNumber), HttpStatus.OK);
    }
}
