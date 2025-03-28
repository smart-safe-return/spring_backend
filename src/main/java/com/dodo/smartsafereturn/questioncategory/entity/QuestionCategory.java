package com.dodo.smartsafereturn.questioncategory.entity;

import com.dodo.smartsafereturn.question.entity.Question;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
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
public class QuestionCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_category_id")
    private Long id;

    private String category;

    @ColumnDefault("false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "questionCategory")
    private List<Question> questionList = new ArrayList<>();

    // 양방향 연관관계 편의 메서드 (Question)
    public void addQuestion(Question question) {
        this.questionList.add(question);
        question.setQuestionCategory(this);
    }
}
