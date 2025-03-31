package com.dodo.smartsafereturn.admin.service;

import com.dodo.smartsafereturn.admin.dto.AdminCreateDto;
import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.admin.dto.AdminUpdateDto;

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
}
