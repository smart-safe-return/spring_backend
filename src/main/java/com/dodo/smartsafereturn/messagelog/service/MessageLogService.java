package com.dodo.smartsafereturn.messagelog.service;

import com.dodo.smartsafereturn.messagelog.dto.MessageLogCreateDto;
import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;

import java.util.List;

public interface MessageLogService {

    // sms 메시지 등록
    void save(MessageLogCreateDto dto);
    // sms 메시지 삭제
    void delete(Long messageLogId);
    // sms 메시지 한건 조회
    MessageLogResponseDto getMessage(Long messageLogId);
    // sms 메시지 귀가 루트별 조회
    List<MessageLogResponseDto> getMessagesBySafeRouteId(Long safeRouteId);
    // sms 메시지 회원별 조회
    List<MessageLogResponseDto> getMessagesByMemberNumber(Long memberNumber);
    // sms 메시지 전체 조회 (ADMIN 전용)
    List<MessageLogResponseDto> getMessages();
}
