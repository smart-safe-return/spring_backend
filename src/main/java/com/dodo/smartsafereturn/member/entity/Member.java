package com.dodo.smartsafereturn.member.entity;

import com.dodo.smartsafereturn.emergencycontact.entity.EmergencyContact;
import com.dodo.smartsafereturn.global.utils.BaseTimeEntity;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.sosmessage.entity.SosMessage;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Slf4j
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_number")
    private Long memberNumber;
    @Column(nullable = false, unique = true)
    private String id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyContact> emergencyContactList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SosMessage sosMessage;

    // 생성자 -> 첫 가입
    public Member(String id, String encodedPassword, String phone) {
        this.id = id;
        this.password = encodedPassword;
        this.phone = phone;
        this.isDeleted = false;
    }

    // 회원 수정 : 비밀번호 or 휴대폰 번호
    public void updateMember(MemberUpdateDto dto) {
        if (dto.getPassword() != null && !dto.getPassword().isEmpty() && !dto.getPassword().isBlank()) {
            this.password = dto.getPassword();
        }
        if (dto.getPhone() != null && !dto.getPhone().isEmpty() && !dto.getPhone().isBlank()) {
            this.phone = dto.getPhone();
        }
    }

    // 양방향 연관관계 편의 메서드 (EmergencyContact)
    public void addEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContactList.add(emergencyContact);
        emergencyContact.setMember(this);
    }

    // 양방향 연관관계 편의 메서드 (SosMessage)
    public void addSosMessage(SosMessage sosMessage) {
        this.sosMessage = sosMessage;
        sosMessage.setMember(this);
    }
}
