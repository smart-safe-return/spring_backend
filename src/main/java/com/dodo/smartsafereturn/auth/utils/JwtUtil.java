package com.dodo.smartsafereturn.auth.utils;

import io.jsonwebtoken.Jwts;
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
     * - 토큰의 타입 (엑세스 토큰 or 리프레시 토큰) 가져오기
     * @param token
     * @return
     */
    public String getType(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }

    /**
     * - 보통은 토큰 생성을 무조건 user 만 생성해주는것으로 한다
     * @param memberNumber
     * @param id
     * @return
     */
    public String generateToken(String tokenType, Long memberNumber, String id, String role, Long expiration) {
        return Jwts.builder()
                .header()
                    .add("typ", "JWT")
                .and()
                .claim("type", tokenType) // access / refresh
                .claim("memberNumber", memberNumber)
                .claim("id", id)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }
}
