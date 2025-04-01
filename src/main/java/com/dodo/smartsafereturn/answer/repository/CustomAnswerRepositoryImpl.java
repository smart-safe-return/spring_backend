package com.dodo.smartsafereturn.answer.repository;

import com.dodo.smartsafereturn.answer.dto.AnswerResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerSearchCondition;
import com.dodo.smartsafereturn.answer.dto.QAnswerResponseDto;
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

import static com.dodo.smartsafereturn.admin.entity.QAdmin.admin;
import static com.dodo.smartsafereturn.answer.entity.QAnswer.answer;
import static com.dodo.smartsafereturn.question.entity.QQuestion.question;


@Slf4j
@RequiredArgsConstructor
public class CustomAnswerRepositoryImpl implements CustomAnswerRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<AnswerResponseDto> getPageByCondition(AnswerSearchCondition condition, Pageable pageable) {
        // 컨텐츠 조회 쿼리
        List<AnswerResponseDto> content = query
                .select(new QAnswerResponseDto(
                        answer.id,
                        answer.admin.id,
                        answer.question.id,
                        answer.question.title,
                        answer.title,
                        answer.content,
                        answer.isDeleted,
                        answer.createdDate,
                        answer.modifiedDate
                ))
                .from(answer)
                .join(answer.admin, admin)
                .join(answer.question, question)
                .where(
                        titleContains(condition.getTitle()),
                        contentContains(condition.getContent()),
                        adminIdEq(condition.getAdminId()),
                        answer.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable))
                .fetch();

        // 카운터 쿼리 최적화
        JPAQuery<Long> count = query
                .select(Wildcard.count) // 5.0 이후부터는 Wildcard 사용
                .from(answer)
                .join(answer.admin, admin)
                .where(
                        titleContains(condition.getTitle()),
                        contentContains(condition.getContent()),
                        adminIdEq(condition.getAdminId()),
                        answer.isDeleted.isFalse()
                );
        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);

    }

    @Override
    public List<AnswerResponseDto> getListByCondition(AnswerSearchCondition condition) {
        return query
                .select(new QAnswerResponseDto(
                        answer.id,
                        answer.admin.id,
                        answer.question.id,
                        answer.question.title,
                        answer.title,
                        answer.content,
                        answer.isDeleted,
                        answer.createdDate,
                        answer.modifiedDate
                ))
                .from(answer)
                .join(answer.admin, admin)
                .join(answer.question, question)
                .where(
                        titleContains(condition.getTitle()),
                        contentContains(condition.getContent()),
                        adminIdEq(condition.getAdminId()),
                        answer.isDeleted.isFalse()
                )
                .fetch();
    }

    // 동적 검색 조건 메소드
    private BooleanExpression titleContains(String title) {
        return title != null ? answer.title.contains(title) : null;
    }

    private BooleanExpression contentContains(String content) {
        return content != null ? answer.content.contains(content) : null;
    }

    private BooleanExpression adminIdEq(String adminId) {
        return adminId != null ? answer.admin.id.eq(adminId) : null;
    }

    // 동적 정렬 메소드
    private OrderSpecifier[] getOrderSpecifier(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            List<OrderSpecifier> orders = new ArrayList<>();

            pageable.getSort().forEach(order -> {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "adminId":
                        orders.add(new OrderSpecifier(direction, answer.admin.id));
                        break;
                    case "title":
                        orders.add(new OrderSpecifier(direction, answer.title));
                        break;
                    case "createdDate":
                        orders.add(new OrderSpecifier(direction, answer.createdDate));
                        break;
                    case "modifiedDate":
                        orders.add(new OrderSpecifier(direction, answer.modifiedDate));
                        break;
                    // 기타 정렬 조건들 추가 가능
                }
            });

            return orders.toArray(new OrderSpecifier[0]);
        }

        // 기본 정렬: 최신순
        return new OrderSpecifier[] {new OrderSpecifier(Order.DESC, answer.createdDate)};
    }

}
