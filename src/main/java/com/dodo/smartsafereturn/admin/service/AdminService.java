package com.dodo.smartsafereturn.admin.service;

import com.dodo.smartsafereturn.admin.dto.AdminCreateDto;
import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.admin.dto.AdminUpdateDto;
import com.dodo.smartsafereturn.admin.entity.Admin;

import java.util.List;

public interface AdminService {

    // 관리자 등록
    void createAdmin(AdminCreateDto createDto);
    // 관리자 수정 (비밀번호)
    void updateAdmin(AdminUpdateDto updateDto);
    // 관리자 삭제 (delete flag 변경)
    void deleteAdmin(Long adminNumber);
    // 관리자 정보 불러오기 (한건)
    AdminResponseDto getAdmin(Long adminNumber);
    // 관리자 정보 불러오기 (리스트)
    List<AdminResponseDto> getAdmins();
    // ID값으로 엔티티 가져오기
    Admin findById(Long adminNumber);
}
