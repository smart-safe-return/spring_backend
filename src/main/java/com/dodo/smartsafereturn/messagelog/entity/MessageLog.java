package com.dodo.smartsafereturn.messagelog.entity;

import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MessageLog extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_log_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    // safeRoute 와 양방향
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safe_route_id")
    private SafeRoute safeRoute;

    // 첫 생성
    @Builder
    public MessageLog(String message, SafeRoute safeRoute) {
        this.message = message;
        this.safeRoute = safeRoute;
    }
}
