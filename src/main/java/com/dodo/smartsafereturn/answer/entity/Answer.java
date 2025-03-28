package com.dodo.smartsafereturn.answer.entity;

import com.dodo.smartsafereturn.admin.entity.Admin;
import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Answer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ColumnDefault("false") // 기본값 false 설정
    private Boolean isDeleted;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_number")
    private Admin admin;

    // 양방향 연관관계 편의 메서드 (Question)
    public void addQuestion(Question question) {
        this.setQuestion(question);
        question.setAnswer(this);
    }
}
