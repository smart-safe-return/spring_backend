package com.dodo.smartsafereturn.sosmessage.controller;

import com.dodo.smartsafereturn.sosmessage.dto.SosMessageCreateDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageResponseDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageUpdateDto;
import com.dodo.smartsafereturn.sosmessage.service.SosMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sos-message")
@RequiredArgsConstructor
public class SosMessageController {

    private final SosMessageService service;

    // sos 메시지 등록
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody SosMessageCreateDto dto) {
        service.createSosMessage(dto);
        return ResponseEntity.ok().build();
    }

    // sos 메시지 수정
    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody SosMessageUpdateDto dto, @PathVariable String id) {
        service.updateSosMessage(dto);
        return ResponseEntity.ok().build();
    }
    // sos 메시지 삭제
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteSosMessage(id);
        return ResponseEntity.ok().build();
    }

    // sos 메시지 조회 (한 회원에 등록된 메시지)
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<?> getSosMessageForMember(@PathVariable Long memberNumber) {
        SosMessageResponseDto message = service.getSosMessage(memberNumber);
        return ResponseEntity.ok(message);
    }

}
