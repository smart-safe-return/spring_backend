package com.dodo.smartsafereturn.admin.service;

import com.dodo.smartsafereturn.admin.dto.AdminCreateDto;
import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.admin.dto.AdminUpdateDto;
import com.dodo.smartsafereturn.admin.entity.Admin;
import com.dodo.smartsafereturn.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createAdmin(AdminCreateDto createDto) {

        // 이미 회원 가입된 관리자 인지 확인 (ID값)
        boolean isExist = adminRepository.existsByIdAndIsDeletedIsFalse(createDto.getId());
        if (isExist) {
            log.info("[AdminService] createAdmin() 예외 발생 : [관리자 등록 로직] 이미 존재하는 관리자 입니다");
            throw  new RuntimeException("[관리자 등록 로직] 이미 존재하는 관리자 입니다");
        }

        adminRepository.save(
                Admin.builder()
                        .id(createDto.getId())
                        .password(passwordEncoder.encode(createDto.getPassword()))
                        .build()
        );
    }

    @Transactional
    @Override
    public void updateAdmin(AdminUpdateDto updateDto) {

        Admin admin = adminRepository.findByAdminNumberAndIsDeletedIsFalse(updateDto.getAdminNumber())
                .orElseThrow(() -> new RuntimeException("[AdminService] getAdmin : 존재하지 않는 관리자"));

        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            // 비밀번호 바꾸기
            admin.update(passwordEncoder.encode(updateDto.getPassword()));
        }
    }

    @Transactional
    @Override
    public void deleteAdmin(Long adminNumber) {

        Admin admin = adminRepository.findByAdminNumberAndIsDeletedIsFalse(adminNumber)
                .orElseThrow(() -> new RuntimeException("[AdminService] getAdmin : 존재하지 않는 관리자"));

        // 활성화일때만 탈퇴로 바뀌도록.
        if (!admin.getIsDeleted()) {
            admin.changeDeleteFlag();
        }
    }

    @Override
    public AdminResponseDto getAdmin(Long adminNumber) {

        Admin admin = adminRepository.findByAdminNumberAndIsDeletedIsFalse(adminNumber)
                .orElseThrow(() -> new RuntimeException("[AdminService] getAdmin : 존재하지 않는 관리자"));

        return AdminResponseDto.builder()
                .adminNumber(admin.getAdminNumber())
                .id(admin.getId())
                .createDate(admin.getCreatedDate())
                .build();
    }

    @Override
    public List<AdminResponseDto> getAdmins() {

        return adminRepository.getList()
                .stream()
                .map(
                        admin -> AdminResponseDto
                                .builder()
                                .adminNumber(admin.getAdminNumber())
                                .id(admin.getId())
                                .createDate(admin.getCreatedDate())
                                .build()
                )
                .toList();
    }

    @Override
    public Admin findById(Long adminNumber) {
        return adminRepository.findByAdminNumberAndIsDeletedIsFalse(adminNumber)
                .orElseThrow(() -> new RuntimeException("[AdminService] getAdmin : 존재하지 않는 관리자"));
    }
}
