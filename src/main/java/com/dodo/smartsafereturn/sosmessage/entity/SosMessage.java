package com.dodo.smartsafereturn.sosmessage.entity;

import com.dodo.smartsafereturn.global.utils.BaseTimeEntity;
import com.dodo.smartsafereturn.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Slf4j
public class SosMessage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sos_message_id")
    private Long id;

    private String content;

    private Boolean isDeleted;

    // 연관관계 편의 메서드를 위한 setter
    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    // 생성자
    public SosMessage(String content, Member member) {
        this.content = content;
        this.member = member;
        this.isDeleted = false;
    }

}
