package com.dodo.smartsafereturn.sosmessage.controller;

import com.dodo.smartsafereturn.sosmessage.dto.SosMessageCreateDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageResponseDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageUpdateDto;
import com.dodo.smartsafereturn.sosmessage.service.SosMessageService;
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
@RequestMapping("/api/sos-message")
@RequiredArgsConstructor
@Tag(name = "SOS 비상 메시지 관리", description = "SOS 비상 메시지 CRUD API")
public class SosMessageController {

    private final SosMessageService service;

    // SOS 메시지 등록
    @Operation(
            summary = "SOS 비상 메시지 등록",
            description = "회원의 SOS 비상 메시지를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 SOS 메시지 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SosMessageCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "SOS 메시지 등록 요청 예시",
                                            summary = "SOS 비상 메시지 등록에 필요한 데이터",
                                            value = "{\n" +
                                                    "  \"member_number\": 1,\n" +
                                                    "  \"content\": \"긴급 상황입니다. 현재 위치에서 도움이 필요합니다.\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SOS 메시지 등록 성공"
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
    public ResponseEntity<?> create(@Validated @RequestBody SosMessageCreateDto dto) {
        service.createSosMessage(dto);
        return ResponseEntity.ok().build();
    }

    // SOS 메시지 수정
    @Operation(
            summary = "SOS 비상 메시지 수정",
            description = "기존 SOS 비상 메시지의 내용을 수정합니다.",
            parameters = {
                    @Parameter(name = "id", description = "수정할 SOS 메시지 ID", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 SOS 메시지 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SosMessageUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "SOS 메시지 수정 요청 예시",
                                            summary = "SOS 비상 메시지 수정에 필요한 데이터",
                                            value = "{\n" +
                                                    "  \"sos_message_id\": 1,\n" +
                                                    "  \"content\": \"긴급 상황입니다. 현재 강남역 인근에서 도움이 필요합니다.\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SOS 메시지 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "SOS 메시지를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Validated @RequestBody SosMessageUpdateDto dto, @PathVariable String id) {
        service.updateSosMessage(dto);
        return ResponseEntity.ok().build();
    }

    // SOS 메시지 삭제
    @Operation(
            summary = "SOS 비상 메시지 삭제",
            description = "SOS 비상 메시지를 삭제합니다.",
            parameters = {
                    @Parameter(name = "id", description = "삭제할 SOS 메시지 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SOS 메시지 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "SOS 메시지를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteSosMessage(id);
        return ResponseEntity.ok().build();
    }

    // SOS 메시지 조회 (한 회원에 등록된 메시지)
    @Operation(
            summary = "회원별 SOS 비상 메시지 조회",
            description = "특정 회원의 SOS 비상 메시지를 조회합니다.",
            parameters = {
                    @Parameter(name = "memberNumber", description = "회원 번호", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SOS 메시지 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SosMessageResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "SOS 메시지 응답 예시",
                                                    summary = "회원의 SOS 비상 메시지 정보",
                                                    value = "{\n" +
                                                            "  \"sos_message_id\": 1,\n" +
                                                            "  \"content\": \"긴급 상황입니다. 현재 강남역 인근에서 도움이 필요합니다.\",\n" +
                                                            "  \"create_date\": \"2025-04-03T16:20:15.789Z\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원을 찾을 수 없거나 해당 회원의 SOS 메시지가 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<?> getSosMessageForMember(@PathVariable Long memberNumber) {
        SosMessageResponseDto message = service.getSosMessage(memberNumber);
        return ResponseEntity.ok(message);
    }
}