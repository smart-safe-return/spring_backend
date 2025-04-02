package com.dodo.smartsafereturn.questioncategory.entity;

import com.dodo.smartsafereturn.question.entity.Question;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class QuestionCategory {

    @Id
    @Column(name = "question_category_id", columnDefinition = "serial")
    private Long id;

    private String category;

    @ColumnDefault("false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "questionCategory")
    private List<Question> questionList = new ArrayList<>();

    @Builder
    public QuestionCategory(String category) {
        this.category = category;
        this.isDeleted = false;
    }

    // 양방향 연관관계 편의 메서드 (Question)
    public void addQuestion(Question question) {
        this.questionList.add(question);
        question.setQuestionCategory(this);
    }

    public void update(String category) {
        this.category = category;
    }

    public void changeIsDeleted() {
        this.isDeleted = !this.isDeleted;
    }
}
