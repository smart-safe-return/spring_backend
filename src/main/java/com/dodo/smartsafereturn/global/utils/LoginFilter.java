package com.dodo.smartsafereturn.global.utils;

import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * 로그인 필터 -> security 필터체인을 통해 로그인을 성공했을 경우 JWT를 발급한다
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final Long accessExpiration;

    public LoginFilter(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       MemberRepository memberRepository,
                       Long accessExpiration) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.accessExpiration = accessExpiration;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // client 에서 username, password 추출
        String id = obtainUsername(request);
        String password = obtainPassword(request);

        // 아이디 비번 검증을 위해 token을 담아 인증 매니저에 전달
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password);
        return authenticationManager.authenticate(authToken);
    }


    // 로그인 성공 시 실행하는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String id = customUserDetails.getUsername();
        Member member = memberRepository.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new UsernameNotFoundException("[LoginFilter] user not found error"));
        Long memberNumber = member.getMemberNumber();
        String role = authResult.getAuthorities().iterator().next().getAuthority();
        // token 생성
        String accessToken = jwtUtil.generateToken(memberNumber, id, role, accessExpiration);
        // Authorization 헤더에 "Bearer " 붙여서 토큰 보내야함 -> HTTP 인증 방식을 RFC 7235 정의해놓은 규격이 있어서 맞춰주는게 좋음
        response.addHeader("Authorization", "Bearer " + accessToken);
    }


    // 로그인 실패 시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("[loginFilter] Unsuccessful authentication");
        // 응답에 401 에러 보내기
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
