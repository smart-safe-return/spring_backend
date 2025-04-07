package com.dodo.smartsafereturn.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Token {
    /**
     * 레디스와 달리 RDB 사용함으로 TTL 시간 설정이 불가능.
     * todo 데이터가 계속 쌓이므로 스케쥴링을 통해 관리하는 기능이 필요함
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    // unique 설정 X : 하나의 회원이 여러 기기로 리프레시 토큰을 가질 수 있음
    private String memberId;

    @Column(columnDefinition = "TEXT")
    private String token;

    private String expiration;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    @Builder
    public Token(String memberId, String token, String expiration, TokenType type) {
        this.memberId = memberId;
        this.token = token;
        this.expiration = expiration;
        this.type = type;
    }
}
