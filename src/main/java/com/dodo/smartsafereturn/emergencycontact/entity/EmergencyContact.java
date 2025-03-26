package com.dodo.smartsafereturn.emergencycontact.entity;

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
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emergency_contact_id")
    private Long id;

    private String name;

    private String phone;

    // 연관관계 편의 메서드를 위한 setter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    // 생성자
    public EmergencyContact(String name, String phone, Member member) {
        this.name = name;
        this.phone = phone;
        this.member = member;
    }

}
