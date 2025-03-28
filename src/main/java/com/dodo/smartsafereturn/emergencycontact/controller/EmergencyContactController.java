package com.dodo.smartsafereturn.emergencycontact.controller;

import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactCreateDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactResponseDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactUpdateDto;
import com.dodo.smartsafereturn.emergencycontact.entity.EmergencyContact;
import com.dodo.smartsafereturn.emergencycontact.service.EmergencyContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/emergency-contact") // 케밥케이스 (보통 권장)
@RequiredArgsConstructor
public class EmergencyContactController {

    private final EmergencyContactService service;

    // sos 비상연락망 등록
    @PostMapping("")
    public ResponseEntity<?> createEmergencyContact(@RequestBody EmergencyContactCreateDto dto) {
        service.create(dto);
        return ResponseEntity.ok().build();
    }

    // sos 비상연락망 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmergencyContact(@PathVariable Long id, @RequestBody EmergencyContactUpdateDto dto) {
        service.update(dto);
        return ResponseEntity.ok().build();
    }

    // sos 비상연락망 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmergencyContact(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // sos 비상연락망 조회 (마이 페이지 - 회원이 등록한 sos 비상 연락망 조회)
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<EmergencyContactResponseDto>> getEmergencyContact(@PathVariable Long memberNumber) {

        return new ResponseEntity<>(service.getMemberContacts(memberNumber), HttpStatus.OK);
    }
}
