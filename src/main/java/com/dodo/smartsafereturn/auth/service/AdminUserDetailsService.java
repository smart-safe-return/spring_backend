package com.dodo.smartsafereturn.auth.service;

import com.dodo.smartsafereturn.admin.entity.Admin;
import com.dodo.smartsafereturn.admin.repository.AdminRepository;
import com.dodo.smartsafereturn.auth.dto.JwtMemberInfoDto;
import com.dodo.smartsafereturn.auth.dto.Role;
import com.dodo.smartsafereturn.auth.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다: " + id));

        return new CustomUserDetails(
                JwtMemberInfoDto.builder()
                .memberNumber(admin.getAdminNumber())
                .id(admin.getId())
                .password(admin.getPassword())
                .role(Role.ROLE_ADMIN.getValue()) // 관리자 역할 지정
                .build());
    }
}
