package com.dodo.smartsafereturn.question.entity;

import com.dodo.smartsafereturn.answer.entity.Answer;
import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.question.dto.QuestionUpdateDto;
import com.dodo.smartsafereturn.questioncategory.entity.QuestionCategory;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Question extends BaseTimeEntity {

    @Id
    @Column(name = "question_id", columnDefinition = "serial")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    @ColumnDefault("false") // 기본값 false 설정
    private Boolean isDeleted;

    @Setter // 연관관계 메서드 설정을 위함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_category_id")
    private QuestionCategory questionCategory;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;
    
    // todo JPA OneToOne 연관관계 주인이 아닌 곳에서 Eager 로딩 강제되는 것 생각해보기
    @Setter
    @OneToOne(mappedBy = "question")
    private Answer answer;

    // 첫 생성
    @Builder
    public Question(String title, String content, QuestionCategory questionCategory, Member member) {
        this.title = title;
        this.content = content;
        this.isDeleted = Boolean.FALSE;
        this.questionCategory = questionCategory;
        this.status = QuestionStatus.IN_PROGRESS;
        this.member = member;
    }

    // update 메서드
    public void update(String title, String content, QuestionCategory questionCategory) {

        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
        if (content != null && !content.isEmpty()) {
            this.content = content;
        }
        if (questionCategory != null) {
            this.questionCategory = questionCategory;
            // 연관관계 추가
            questionCategory.addQuestion(this);
        }
    }

    public void changeDeleteFlag() {
        this.isDeleted = !this.isDeleted;
    }

    public void updateStatus(QuestionStatus changedStatus) {
        this.status = changedStatus;
    }
}
