package com.dodo.smartsafereturn.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RefreshToken {
    /**
     * 레디스와 달리 RDB 사용함으로 TTL 시간 설정이 불가능.
     * todo 데이터가 계속 쌓이므로 스케쥴링을 통해 관리하는 기능이 필요함
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    // unique 설정 X : 하나의 회원이 여러 기기로 리프레시 토큰을 가질 수 있음
    private String memberId;

    @Column(columnDefinition = "TEXT")
    private String refresh;

    private String expiration;

    @Builder
    public RefreshToken(String memberId, String refresh, String expiration) {
        this.memberId = memberId;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
