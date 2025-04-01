package com.dodo.smartsafereturn.auth.utils;

import com.dodo.smartsafereturn.auth.dto.JwtType;
import com.dodo.smartsafereturn.auth.entity.CustomUserDetails;
import com.dodo.smartsafereturn.auth.dto.LoginDto;
import com.dodo.smartsafereturn.auth.entity.RefreshToken;
import com.dodo.smartsafereturn.auth.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

/**
 * 로그인 필터 -> security 필터체인을 통해 로그인을 성공했을 경우 JWT를 발급한다
 *
 * 프론트 입장 JSON 로그인 요청 body
 * {
 *     "id" : "로그인할 아이디",
 *     "password" : "로그인할 비밀번호"
 * }
 * 
 * access / refresh token 구현
 * - 모바일 앱과의 통신 고려 -> 모두 HTTP 헤더로 전송함
 * - 엑세스 토큰 : Authorization 헤더
 * - 리프레시 토큰 : refresh 헤더
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Long accessExpiration;
    private final Long refreshExpiration;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       Long accessExpiration,
                       Long refreshExpiration,
                       RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginDto loginDto = null;

        try {
            // JSON 요청 본문을 읽어서 LoginDto 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            log.error("[LoginFilter] JSON parsing failed", e);
            throw new RuntimeException("[LoginFilter] JSON 파싱 실패: " + e.getMessage());
        }

        // LoginDto 에서 id와 password 추출
        String id = loginDto.getId();
        String password = loginDto.getPassword();

        // 값 유효성 검사 -> NullPointerException 방지로 빈문자열에 대한 예외만 다루어서 예외 처리 통일
        if (id == null) {
            id = "";
        }
        if (password == null) {
            password = "";
        }

        // 아이디 비번 검증을 위해 token 을 담아 인증 매니저에 전달
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password);
        return authenticationManager.authenticate(authToken);
    }


    // 로그인 성공 시 실행하는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // userDetails 로그인 정보 (UserDetailsService 에서 DB 검증 한번 받고옴)
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        // Claims 세팅
        String id = customUserDetails.getUsername();
        Long memberNumber = customUserDetails.getMemberInfoDto().getMemberNumber();
        String role = authResult.getAuthorities().iterator().next().getAuthority();

        // access token & refresh token 생성
        String accessToken = jwtUtil.generateToken(JwtType.ACCESS.getValue(), memberNumber, id, role, accessExpiration);
        String refreshToken = jwtUtil.generateToken(JwtType.REFRESH.getValue(), memberNumber, id, role, refreshExpiration);

        // 리프레시 토큰 저장소에 생성한 토큰 저장
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .memberId(id)
                        .refresh(refreshToken)
                        .expiration(new Date(System.currentTimeMillis() + refreshExpiration).toString())
                        .build()
        );

        // Authorization 헤더에 "Bearer " 붙여서 토큰 보내야함 -> HTTP 인증 방식을 RFC 7235 정의해놓은 규격이 있어서 맞춰주는게 좋음
        // 리프레시 토큰 또한 refresh 헤더에 모두 보내줌
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("refresh", "Bearer " + refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    // 로그인 실패 시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("[loginFilter] Unsuccessful authentication");
        // 응답에 401 에러 보내기
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
