package com.dodo.smartsafereturn.messagelog.controller;


import com.dodo.smartsafereturn.messagelog.dto.MessageLogCreateDto;
import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;
import com.dodo.smartsafereturn.messagelog.service.MessageLogService;
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
public class MessageLogController {

    private final MessageLogService messageLogService;

    // sms 메시지 등록
    @PostMapping("")
    public ResponseEntity<?> save(@Validated @RequestBody MessageLogCreateDto dto) {
        messageLogService.save(dto);
        return ResponseEntity.ok().build();
    }

    // sms 메시지 삭제
    @DeleteMapping("/{messageLogId}")
    public ResponseEntity<?> delete(@PathVariable Long messageLogId) {
        messageLogService.delete(messageLogId);
        return ResponseEntity.ok().build();
    }

    // sms 메시지 한건 조회
    @GetMapping("/{messageLogId}")
    public ResponseEntity<MessageLogResponseDto> get(@PathVariable Long messageLogId) {
        return ResponseEntity.ok(messageLogService.getMessage(messageLogId));
    }

    // sms 메시지 귀가 루트별 조회
    @GetMapping("/safe-route/{safeRouteId}")
    public ResponseEntity<List<MessageLogResponseDto>> getMessagesBySafeRoute(@PathVariable Long safeRouteId) {
        return ResponseEntity.ok(messageLogService.getMessagesBySafeRouteId(safeRouteId));
    }

    // sms 메시지 회원별 조회
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<MessageLogResponseDto>> getMessagesByMember(@PathVariable Long memberNumber) {
        return ResponseEntity.ok(messageLogService.getMessagesByMemberNumber(memberNumber));
    }

    // sms 메시지 전체 조회
    @GetMapping("")
    public ResponseEntity<List<MessageLogResponseDto>> getMessages() {
        return ResponseEntity.ok(messageLogService.getMessages());
    }
}
