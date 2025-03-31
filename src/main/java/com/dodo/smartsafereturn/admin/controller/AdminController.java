package com.dodo.smartsafereturn.admin.controller;

import com.dodo.smartsafereturn.admin.dto.AdminCreateDto;
import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.admin.dto.AdminUpdateDto;
import com.dodo.smartsafereturn.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자 등록
    @PostMapping("")
    public ResponseEntity<?> createAdmin(@Validated @RequestBody AdminCreateDto dto) {
        adminService.createAdmin(dto);
        return ResponseEntity.ok().build();
    }

    // 관리자 수정 (비밀번호)
    @PutMapping("/{adminNumber}")
    public ResponseEntity<?> updateAdmin(@Validated @RequestBody AdminUpdateDto dto) {
        adminService.updateAdmin(dto);
        return ResponseEntity.ok().build();
    }

    // 관리자 삭제 (delete flag 변경)
    @DeleteMapping("/{adminNumber}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminNumber) {
        adminService.deleteAdmin(adminNumber);
        return ResponseEntity.ok().build();
    }

    // 관리자 정보 불러오기 (한건)
    @GetMapping("/{adminNumber}")
    public ResponseEntity<?> getAdmin(@PathVariable Long adminNumber) {
        return ResponseEntity.ok().body(adminService.getAdmin(adminNumber));
    }

    // 관리자 정보 불러오기 (리스트)
    @GetMapping("")
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.ok().body(adminService.getAdmins());
    }
}
