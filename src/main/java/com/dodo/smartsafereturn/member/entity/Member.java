package com.dodo.smartsafereturn.member.entity;

import com.dodo.smartsafereturn.emergencycontact.entity.EmergencyContact;
import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.question.entity.Question;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String profile;
    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmergencyContact> emergencyContactList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SafeRoute> safeRouteList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questionList = new ArrayList<>();


    // 생성자 -> 첫 가입
    @Builder
    public Member(String id, String password, String phone, String profile) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.isDeleted = false;
        this.profile = profile;
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

    // 양방향 연관관계 편의 메서드 (SafeRoute)
    public void addSafeRoute(SafeRoute safeRoute) {
        this.safeRouteList.add(safeRoute);
        safeRoute.setMember(this);
    }

    // 양방향 연관관계 편의 메서드 (Question)
    public void addQuestion(Question question) {
        this.questionList.add(question);
        question.setMember(this);
    }

    // 회원 삭제 or 복원 처리 -> isDeleted 플래그 변경
    public void changeDeleteFlag() {

        this.isDeleted = !this.isDeleted;
    }
}
