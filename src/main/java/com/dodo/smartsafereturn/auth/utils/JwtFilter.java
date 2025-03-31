package com.dodo.smartsafereturn.auth.utils;

import com.dodo.smartsafereturn.auth.entity.CustomUserDetails;
import com.dodo.smartsafereturn.auth.dto.JwtMemberInfoDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 검증 필터 - JWT 갖고 인가 요청에 대해 처리
 * 2025-03-27 : access / refresh token 구현
 * 기능 : 요청 헤더 Authorization 에서 JWT 추출 -> 강제로 SecurityContextHolder 세션 생성 (STATELESS 상태로 관리)
 * - 1. 액세스 토큰이 만료된 경우 -> 리프레시 토큰 검증 요청을 다시 프론트로 보내야함
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청에서 Authorization 헤더 찾기 (JWT 인증이 필요한 요청임)
        String authorization = request.getHeader("Authorization");

        // Authorization 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // Authorization 이 없으므로 JWT 검증을 안하고 메서드 종료
            filterChain.doFilter(request, response);
            return;
        }

        // access token 획득
        String accessToken = authorization.split(" ")[1];


        // 토큰 소멸시간 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            // response body
            response.getWriter().print("access_expired");
            // status code 설정 todo 엑세스 토큰 만료 -> 프론트와 협의한 에러 코드를 보내서 리프레시토큰을 요구하도록 함 (일단 401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
            // 필터를 절대 넘기지않고 만료 응답 코드를 프론트로 그대로 넘겨줌
        }

        // 토큰이 access token 인지 검증
        String type = jwtUtil.getType(accessToken);
        if (!type.equals("access")) {
            // response body
            response.getWriter().print("access_invalid");
            // status code 설정 todo 엑세스 토큰이 잘못됨 -> 프론트와 협의한 에러 코드를 보내서 리프레시토큰을 요구하도록 함 (일단 401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰에서 claims 얻기
        Long memberNumber = jwtUtil.getMemberNumber(accessToken);
        String id = jwtUtil.getId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        JwtMemberInfoDto dto = JwtMemberInfoDto.builder()
                .memberNumber(memberNumber)
                .id(id)
                .role(role)
                .build();

        // UserDetails 에 정보 넣기
        CustomUserDetails userDetails = new CustomUserDetails(dto);

        // 스프링 시큐리티 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 세션 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 로직 끝 -> 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
}
