package com.dodo.smartsafereturn.auth.utils;

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

        // refresh 타입의 토큰인지 검증
        if (!jwtUtil.getType(refreshToken).equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 만료 검증
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            // 이미 만료되었어도 이미 오랜시간 프론트에서 있다가 로그아웃한 경우가 있으므로 사용자 경험 측면에서 그냥 로그아웃하게 해주는게 맞음
            // 단지 리프레시 토큰 만료만 알려주기
            response.getWriter().write("refresh token expired");
        }

        // 로그아웃 로직 수행
        authService.logout(refreshToken, response);

    }
}
