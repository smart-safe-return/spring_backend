package com.dodo.smartsafereturn.auth.service;

import com.dodo.smartsafereturn.auth.dto.JwtType;
import com.dodo.smartsafereturn.auth.dto.RefreshValidationResultDto;
import com.dodo.smartsafereturn.auth.entity.Token;
import com.dodo.smartsafereturn.auth.entity.TokenType;
import com.dodo.smartsafereturn.auth.repository.TokenRepository;
import com.dodo.smartsafereturn.auth.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * - reissue 메서드
 * 리프레시 토큰 기반으로 엑세스 토큰을 재발급 해주는 로직
 * Refresh Token Rotate 기법 추가
 * - 엑세스 토큰 갱신 시, 리프레시 토큰도 함꼐 갱신 -> 토큰 교체로 보안성 강화 및 로그인 지속시간 늘리기 가능
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Value("${jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Override
    public RefreshValidationResultDto reissue(String refreshToken, HttpServletResponse response) {

        RefreshValidationResultDto result = new RefreshValidationResultDto();

        // 리프레시 토큰 전달 여부 검증
        if (refreshToken == null) {
            // 프론트로 응답 코드 반환
            result.setBody("refresh token is null");
            result.setStatus(HttpStatus.BAD_REQUEST);
            return result;
        }

        // "Bearer " 제거
        refreshToken = refreshToken.split(" ")[1];

        // 리프레시 토큰 만료 여부 검증
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            // 만료 응답
            result.setBody("refresh token is expired");
            result.setStatus(HttpStatus.BAD_REQUEST);
            return result;
        }

        // refresh 토큰인지 타입 검증
        if (!jwtUtil.getType(refreshToken).equals("refresh")) {
            result.setBody("invalid refresh token");
            result.setStatus(HttpStatus.BAD_REQUEST);
            return result;
        }

        // 리프레시 토큰 저장소에 등록된 토큰인지 검증 -> 서버측 검증
        Boolean isExist = tokenRepository.existsByToken(refreshToken);
        if (!isExist) {
            log.info("[AuthService] invalid refresh token : 리프레시 토큰 저장소 검증 실패");
            result.setBody("invalid refresh token");
            result.setStatus(HttpStatus.BAD_REQUEST);
            return result;
        }

        // 리프레시 토큰 값으로 엑세스 토큰 생성
        Long memberNumber = jwtUtil.getMemberNumber(refreshToken);
        String id = jwtUtil.getId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 새롭게 엑세스 토큰 발급 후, 다시 Authorization 헤더에 첨부
        String reissuedAccessToken = jwtUtil.generateToken(JwtType.ACCESS.getValue(), memberNumber, id, role, accessExpiration);
        response.addHeader("Authorization", "Bearer " + reissuedAccessToken);

        // RTR 기법 추가 (리프레시 토큰 재발급)
        String reissuedRefreshToken = jwtUtil.generateToken(JwtType.REFRESH.getValue(), memberNumber, id, role, refreshExpiration);
        response.addHeader("refresh", "Bearer " + reissuedRefreshToken);
        /**
         * 주의점 - 재발급을 해도 이전의 토큰을 가지고 서버에 가져가도 인증이 됨 -> 리프레시 토큰 저장소 검증
         */
        tokenRepository.deleteByToken(refreshToken);
        tokenRepository.save(
                Token.builder()
                        .memberId(id)
                        .token(reissuedRefreshToken)
                        .expiration(new Date(System.currentTimeMillis() + refreshExpiration).toString())
                        .type(TokenType.REFRESH_TOKEN)
                        .build()
        );

        result.setBody("access token reissued successfully");
        result.setStatus(HttpStatus.OK);
        return result;
    }

    @Override
    public void logout(String refreshToken, HttpServletResponse response) {
        // DB에 해당 토큰이 있는지 확인
        if (!tokenRepository.existsByToken(refreshToken)) {
            // DB에 없다면 이미 로그아웃된 상태여야하므로 400 던짐
            log.info("[JWT logout 기능] 이미 로그아웃되어 없는 리프레시 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        /**
         * 로그아웃 로직 수행
         * 헤더로 받은 refresh token 을 기반으로 토큰 저장소에서 삭제
         * - 프론트의 경우 -> access , refresh 모두 저장소에서 삭제하고, refresh 만 헤더로 추가해서 logout 경로로 요청하면 끝.
         */
        tokenRepository.deleteByToken(refreshToken);
    }
}
