package com.dodo.smartsafereturn.safeRoute.entity;

import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.messagelog.entity.MessageLog;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteUpdateDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class SafeRoute extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safe_route_id")
    private Long id;

    @Column(nullable = false)
    private String startLocation;

    @Column(nullable = false)
    private String endLocation;

    @Column(name = "is_success", nullable = false)
    @Enumerated(EnumType.STRING)
    private RouteState isSuccess = RouteState.STARTED;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    // 양방향으로 member 연결 -> 회원에서 조회해야 함
    @Setter // 연관관계 메서드 활용을 위해서만 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    // messageLog 와 양방향 연결
    @OneToMany(mappedBy = "safeRoute")
    private List<MessageLog> messageLogList = new ArrayList<>();

    // 생성자 -> 첫 생성
    @Builder
    public SafeRoute(String startLocation, String endLocation, LocalDateTime startTime, LocalDateTime endTime, Member member) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.member = member;
    }

    // update 메서드
    public void update(SafeRouteUpdateDto dto) {
        this.endLocation = dto.getEndLocation();
        this.endTime = dto.getEndTime();
    }

    // status 상태 변경
    public void changeIsSuccess(RouteState isSuccess) {
        this.isSuccess = isSuccess;
    }


    // 양방향 연관관계 편의 메서드 (MessageLog)
    public void addMessageLog(MessageLog messageLog) {
        this.messageLogList.add(messageLog);
        messageLog.setSafeRoute(this);
    }
}
