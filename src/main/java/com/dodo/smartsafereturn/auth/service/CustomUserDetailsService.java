package com.dodo.smartsafereturn.auth.service;

import com.dodo.smartsafereturn.auth.entity.CustomUserDetails;
import com.dodo.smartsafereturn.auth.dto.JwtMemberInfoDto;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberByIdNotDeleted(id)
                .orElseThrow(() -> new UsernameNotFoundException("[CustomUserDetailsService] user not found error"));

        return new CustomUserDetails(
                JwtMemberInfoDto.builder()
                        .id(member.getId())
                        .memberNumber(member.getMemberNumber())
                        .role("USER_ROLE")
                        .password(member.getPassword())
                        .build());
    }
}
