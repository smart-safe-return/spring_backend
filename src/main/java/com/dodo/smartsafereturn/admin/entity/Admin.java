package com.dodo.smartsafereturn.admin.entity;

import com.dodo.smartsafereturn.answer.entity.Answer;
import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Slf4j
public class Admin extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_number")
    private Long adminNumber;

    private String id;

    private String password;

    @ColumnDefault("false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answerList = new ArrayList<>();

    // 양방향 연관관계 편의 메서드 (Question)
    public void addAnswer(Answer answer) {
        this.answerList.add(answer);
        answer.setAdmin(this);
    }
}
