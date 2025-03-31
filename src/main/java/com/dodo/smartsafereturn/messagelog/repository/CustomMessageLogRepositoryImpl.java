package com.dodo.smartsafereturn.messagelog.repository;

import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;
import com.dodo.smartsafereturn.messagelog.dto.QMessageLogResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dodo.smartsafereturn.member.entity.QMember.member;
import static com.dodo.smartsafereturn.messagelog.entity.QMessageLog.messageLog;
import static com.dodo.smartsafereturn.safeRoute.entity.QSafeRoute.safeRoute;

/**
 * QueryDsl 구현을 위함 -> 적용한 커스텀 인터페이스를 spring data jpa 레포지토리에 적용
 */

@RequiredArgsConstructor
public class CustomMessageLogRepositoryImpl implements CustomMessageLogRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MessageLogResponseDto> getMessagesBySafeRouteId(Long safeRouteId) {
        return queryFactory
                .select(
                        new QMessageLogResponseDto(
                                messageLog.id,
                                messageLog.message,
                                messageLog.createdDate
                        )
                )
                .from(messageLog)
                .join(messageLog.safeRoute, safeRoute)
                .where(safeRoute.id.eq(safeRouteId))
                .fetch();
    }

    @Override
    public List<MessageLogResponseDto> getMessagesByMemberNumber(Long memberNumber) {
        return queryFactory
                .select(
                        new QMessageLogResponseDto(
                                messageLog.id,
                                messageLog.message,
                                messageLog.createdDate
                        )
                )
                .from(messageLog)
                .join(messageLog.safeRoute, safeRoute)
                .join(safeRoute.member, member)
                .where(member.memberNumber.eq(memberNumber))
                .fetch();
    }

    @Override
    public List<MessageLogResponseDto> getMessages() {
        return queryFactory
                .select(
                        new QMessageLogResponseDto(
                                messageLog.id,
                                messageLog.message,
                                messageLog.createdDate
                        )
                )
                .from(messageLog)
                .fetch();
    }
}
