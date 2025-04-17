package com.dodo.smartsafereturn.messagelog.controller;


import com.dodo.smartsafereturn.messagelog.dto.MessageLogCreateDto;
import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;
import com.dodo.smartsafereturn.messagelog.service.MessageLogService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/message-log")
@RequiredArgsConstructor
@Tag(name = "SMS 메시지 내역 로그 & SMS API", description = "MessageLog API CRUD 구현 및 SMS 기능 구현")
public class MessageLogController {

    private final MessageLogService messageLogService;

    /**
     * sms 메시지 등록 + coolSms 메시지 보내기
     * - safeRoute 시간 내로 도착 실패 시 -> 프론트에서 /api/message-log [POST] 요청
     */
    @Operation(
            summary = "SMS 메시지 전송 및 로그 등록",
            description = "새로운 SMS 메시지를 전송하고 로그를 등록합니다. 안전 귀가 경로 시간 내 도착 실패 시 프론트에서 호출해야 합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "전송할 SMS 메시지 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageLogCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "SMS 메시지 전송 요청 예시",
                                            summary = "SMS 메시지 전송 및 로그 등록에 필요한 데이터",
                                            value = """
                                                {
                                                  "safeRouteId": 5,
                                                  "message": "[안전 귀가 알림] 홍길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                  "location": {
                                                    "type": "Point",
                                                    "coordinates": [127.1087, 37.3947]
                                                  },
                                                  "phoneList": ["01012345678", "01098765432"]
                                                }
                                                """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "SMS 메시지 전송 및 로그 등록 성공"
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
    public ResponseEntity<?> save(@Validated @RequestBody MessageLogCreateDto dto) {
        messageLogService.save(dto);
        return ResponseEntity.ok().build();
    }

    // sms 메시지 삭제
    @Operation(
            summary = "SMS 메시지 로그 삭제",
            description = "특정 SMS 메시지 로그를 삭제합니다.",
            parameters = {
                    @Parameter(name = "messageLogId", description = "삭제할 메시지 로그 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "메시지 로그 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "메시지 로그를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{messageLogId}")
    public ResponseEntity<?> delete(@PathVariable Long messageLogId) {
        messageLogService.delete(messageLogId);
        return ResponseEntity.ok().build();
    }

    // sms 메시지 한건 조회
    @Operation(
            summary = "SMS 메시지 로그 상세 조회",
            description = "특정 SMS 메시지 로그의 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "messageLogId", description = "조회할 메시지 로그 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "메시지 로그 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageLogResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "메시지 로그 조회 응답 예시",
                                                    summary = "메시지 로그 상세 정보",
                                                    value = """
                                                            {
                                                              "message_log_id": 5,
                                                              "message": "[안전 귀가 알림] 홍길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                              "create_date": "2025-04-03T14:30:45.123Z",
                                                              "location": {
                                                                "type": "Point",
                                                                "coordinates": [127.1087, 37.3947]
                                                              }
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "메시지 로그를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{messageLogId}")
    public ResponseEntity<MessageLogResponseDto> get(@PathVariable Long messageLogId) {
        return ResponseEntity.ok(messageLogService.getMessage(messageLogId));
    }

    // sms 메시지 귀가 루트별 조회
    @Operation(
            summary = "귀가 경로별 SMS 메시지 로그 목록 조회",
            description = "특정 안전 귀가 경로에 대한 모든 SMS 메시지 로그를 조회합니다.",
            parameters = {
                    @Parameter(name = "safeRouteId", description = "안전 귀가 경로 ID", required = true, example = "5")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "메시지 로그 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageLogResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "귀가 경로별 메시지 로그 응답 예시",
                                                    summary = "특정 귀가 경로의 메시지 로그 목록",
                                                    value = """
                                                            [
                                                                {
                                                                  "message_log_id": 1,
                                                                  "message": "[안전 귀가 알림] 홍길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                                  "create_date": "2025-04-03T14:30:45.123Z",
                                                                  "location": {
                                                                    "type": "Point",
                                                                    "coordinates": [127.1087, 37.3947]
                                                                  }
                                                                },
                                                                {
                                                                  "message_log_id": 2,
                                                                  "message": "[안전 귀가 알림] 김길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                                  "create_date": "2025-04-04T14:30:45.123Z",
                                                                  "location": {
                                                                    "type": "Point",
                                                                    "coordinates": [127.1087, 39.3947]
                                                                  }
                                                                }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "안전 귀가 경로를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/safe-route/{safeRouteId}")
    public ResponseEntity<List<MessageLogResponseDto>> getMessagesBySafeRoute(@PathVariable Long safeRouteId) {
        return ResponseEntity.ok(messageLogService.getMessagesBySafeRouteId(safeRouteId));
    }

    // sms 메시지 회원별 조회
    @Operation(
            summary = "회원별 SMS 메시지 로그 목록 조회",
            description = "특정 회원의 모든 SMS 메시지 로그를 조회합니다.",
            parameters = {
                    @Parameter(name = "memberNumber", description = "회원 번호", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "메시지 로그 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageLogResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "회원별 메시지 로그 응답 예시",
                                                    summary = "특정 회원의 메시지 로그 목록",
                                                    value = """
                                                            [
                                                                {
                                                                  "message_log_id": 1,
                                                                  "message": "[안전 귀가 알림] 홍길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                                  "create_date": "2025-04-03T14:30:45.123Z",
                                                                  "location": {
                                                                    "type": "Point",
                                                                    "coordinates": [127.1087, 37.3947]
                                                                  }
                                                                },
                                                                {
                                                                  "message_log_id": 2,
                                                                  "message": "[안전 귀가 알림] 김길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                                  "create_date": "2025-04-04T14:30:45.123Z",
                                                                  "location": {
                                                                    "type": "Point",
                                                                    "coordinates": [127.1087, 39.3947]
                                                                  }
                                                                }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<MessageLogResponseDto>> getMessagesByMember(@PathVariable Long memberNumber) {
        return ResponseEntity.ok(messageLogService.getMessagesByMemberNumber(memberNumber));
    }

    // sms 메시지 전체 조회
    @Operation(
            summary = "전체 SMS 메시지 로그 목록 조회",
            description = "모든 SMS 메시지 로그를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전체 메시지 로그 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageLogResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "전체 메시지 로그 응답 예시",
                                                    summary = "전체 메시지 로그 목록",
                                                    value = """
                                                            [
                                                                {
                                                                  "message_log_id": 1,
                                                                  "message": "[안전 귀가 알림] 홍길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                                  "create_date": "2025-04-03T14:30:45.123Z",
                                                                  "location": {
                                                                    "type": "Point",
                                                                    "coordinates": [127.1087, 37.3947]
                                                                  }
                                                                },
                                                                {
                                                                  "message_log_id": 2,
                                                                  "message": "[안전 귀가 알림] 김길동님이 설정한 시간 내에 도착하지 않았습니다. 현재 위치: 서울시 강남구",
                                                                  "create_date": "2025-04-04T14:30:45.123Z",
                                                                  "location": {
                                                                    "type": "Point",
                                                                    "coordinates": [127.1087, 39.3947]
                                                                  }
                                                                }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "아무튼 예외 발생",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<List<MessageLogResponseDto>> getMessages() {
        return ResponseEntity.ok(messageLogService.getMessages());
    }
}
