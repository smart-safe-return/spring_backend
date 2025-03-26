package com.dodo.smartsafereturn.global.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * - Jwt 생성 및 관리 클래스
 * - jwt claims 내용 : member_number, id, role 3가지
 */
@Slf4j
@Component
public class JwtUtil {

    private SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}")String secret) {
        this.secretKey =
                new SecretKeySpec(
                        secret.getBytes(StandardCharsets.UTF_8),
                        Jwts.SIG.HS256.key().build().getAlgorithm()
                );
    }

    public Long getMemberNumber(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberNumber", Long.class);
    }

    public String getId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class); // 일반회원이면 무조건 ROLE_USER 가짐
    }

    /**
     * @return true : 만료됨
     * @return false : 유효함
     */
    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * - 보통은 토큰 생성을 무조건 user 만 생성해주는것으로 한다
     * @param memberNumber
     * @param id
     * @return
     */
    public String generateToken(Long memberNumber, String id, String role, Long expiration) {
        return Jwts.builder()
                .claim("memberNumber", memberNumber)
                .claim("id", id)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }
}
