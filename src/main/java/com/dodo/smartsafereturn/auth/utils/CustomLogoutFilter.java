package com.dodo.smartsafereturn.auth.utils;

import com.dodo.smartsafereturn.auth.repository.RefreshTokenRepository;
import com.dodo.smartsafereturn.auth.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 커스텀 로그아웃 필터
 * - 로그아웃 로직 수행
 * - JWT refresh 토큰을 헤더로 받아 토큰 저장소에서 삭제
 */
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 로그아웃 요청인지 검증 (/logout)
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/api\\/auth\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 리프레시 토큰 가져오기
        String refreshToken = request.getHeader("refresh");

        // 리프레시 토큰 검증
        if (refreshToken == null) {
            // 400 에러 응답
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Bearer 포장 떼기
        refreshToken = refreshToken.split(" ")[1];

        // 만료 검증
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            // 이미 만료되었으면 이미 로그아웃 된 상태므로 400 에러 or 이미 로그아웃됨을 알려줌
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // refresh 타입의 토큰인지 검증
        if (!jwtUtil.getType(refreshToken).equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 로직 수행
        authService.logout(refreshToken, response);

    }
}
