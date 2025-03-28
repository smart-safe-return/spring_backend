package com.dodo.smartsafereturn.question.entity;

import com.dodo.smartsafereturn.answer.entity.Answer;
import com.dodo.smartsafereturn.global.entity.BaseTimeEntity;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.questioncategory.entity.QuestionCategory;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Slf4j
public class Question extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

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
}
