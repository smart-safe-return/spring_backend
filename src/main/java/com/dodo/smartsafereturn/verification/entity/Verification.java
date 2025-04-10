package com.dodo.smartsafereturn.verification.entity;

import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id")
    private Long id;

    // 인증 코드 -> 랜덤 코드
    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    // 인증 대상 식별자 (휴대폰 번호 or email)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String identifier;

    // 인증 타입 (SMS, EMAIL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationType type;

    // 인증 목적 (PASSWORD, USERID, SIGNUP 등)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationPurpose purpose;

    // 만료 시간
    @Column(nullable = false)
    private LocalDateTime expiryTime;

    // 인증 완료 여부
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean verified;

    // 인증 시도 횟수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int attempts;

    // 첫 생성
    @Builder
    public Verification(String code, String identifier, VerificationType type, VerificationPurpose purpose) {
        this.code = code;
        this.identifier = identifier;
        this.type = type;
        this.purpose = purpose;
        this.expiryTime = LocalDateTime.now().plusMinutes(3); // 유효시간 : 인증 요청 시간  + 3분
    }

    // 인증 횟수 추가 메서드
    public void incrementAttempts() {
        this.attempts++;
    }

    // 만료된 요청인지 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    // 인증 완료 플래그
    public void changeVerified() {
        this.verified = true;
    }
}
