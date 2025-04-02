package com.dodo.smartsafereturn.sosmessage.entity;

import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class SosMessage extends BaseTimeEntity {

    @Id
    @Column(name = "sos_message_id", columnDefinition = "serial")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Boolean isDeleted;

    // 단방향 (sosMessage -> Member)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    // 생성자
    @Builder
    public SosMessage(String content, Member member) {
        this.content = content;
        this.member = member;
        this.isDeleted = false;
    }

    // update 메서드
    public void update(String content) {
        this.content = content;
    }

    // 회원 삭제 or 복원 처리 -> isDeleted 플래그 변경
    public void changeDeleteFlag() {
        this.isDeleted = !this.isDeleted;
    }
}
