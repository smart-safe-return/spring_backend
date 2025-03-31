package com.dodo.smartsafereturn.question.repository;

import com.dodo.smartsafereturn.question.dto.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.dodo.smartsafereturn.member.entity.QMember.member;
import static com.dodo.smartsafereturn.question.entity.QQuestion.question;
import static com.dodo.smartsafereturn.questioncategory.entity.QQuestionCategory.questionCategory;

@Slf4j
@RequiredArgsConstructor
public class CustomQuestionRepositoryImpl implements CustomQuestionRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<QuestionResponseListDto> searchQuestionByCondition(QuestionSearchCondition condition, Pageable pageable) {
        // 컨텐츠 조회 쿼리
        List<QuestionResponseListDto> content = query
                .select(new QQuestionResponseListDto(
                        question.id,
                        question.member.id,
                        question.questionCategory.category,
                        question.title,
                        question.createdDate,
                        question.modifiedDate
                ))
                .from(question)
                .join(question.member, member)
                .join(question.questionCategory, questionCategory)
                .where(
                        titleContains(condition.getTitle()),
                        contentContains(condition.getContent()),
                        categoryTypeEq(condition.getCategory()),
                        memberIdEq(condition.getMemberId()),
                        question.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable))
                .fetch();

        // 카운터 쿼리 최적화
        JPAQuery<Long> count = query
                .select(Wildcard.count) // 5.0 이후부터는 Wildcard 사용
                .from(question)
                .where(
                        titleContains(condition.getTitle()),
                        contentContains(condition.getContent()),
                        categoryTypeEq(condition.getCategory()),
                        memberIdEq(condition.getMemberId())
                );
        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    // 동적 검색 조건 메소드
    private BooleanExpression titleContains(String title) {
        return title != null ? question.title.contains(title) : null;
    }

    private BooleanExpression contentContains(String content) {
        return content != null ? question.content.contains(content) : null;
    }

    private BooleanExpression categoryTypeEq(String categoryType) {
        return categoryType != null ? question.questionCategory.category.eq(categoryType) : null;
    }

    private BooleanExpression memberIdEq(String memberId) {
        return memberId != null ? question.member.id.eq(memberId) : null;
    }

    // 동적 정렬 메소드
    private OrderSpecifier[] getOrderSpecifier(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            List<OrderSpecifier> orders = new ArrayList<>();

            pageable.getSort().forEach(order -> {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "memberId":
                        orders.add(new OrderSpecifier(direction, question.member.id));
                        break;
                    case "title":
                        orders.add(new OrderSpecifier(direction, question.title));
                        break;
                    case "createdDate":
                        orders.add(new OrderSpecifier(direction, question.createdDate));
                        break;
                    case "modifiedDate":
                        orders.add(new OrderSpecifier(direction, question.modifiedDate));
                        break;
                    // 기타 정렬 조건들 추가 가능
                }
            });

            return orders.toArray(new OrderSpecifier[0]);
        }

        // 기본 정렬: 최신순
        return new OrderSpecifier[] {new OrderSpecifier(Order.DESC, question.createdDate)};
    }

    // 한 건 상세 조회
    @Override
    public QuestionResponseDto getQuestionById(Long questionId) {
        return query
                .select(new QQuestionResponseDto(
                        question.id,
                        question.member.id,
                        question.questionCategory.category,
                        question.title,
                        question.content,
                        question.createdDate,
                        question.modifiedDate
                ))
                .from(question)
                .where(
                        question.id.eq(questionId),
                        question.isDeleted.isFalse()
                )
                .fetchOne();
    }
}
