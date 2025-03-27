package com.dodo.smartsafereturn.auth.entity;

import com.dodo.smartsafereturn.auth.dto.JwtMemberInfoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final JwtMemberInfoDto memberInfoDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_USER");
        return authorities;
    }

    @Override
    public String getUsername() {
        return memberInfoDto.getId();
    }

    @Override
    public String getPassword() {
        return memberInfoDto.getPassword();
    }
}
