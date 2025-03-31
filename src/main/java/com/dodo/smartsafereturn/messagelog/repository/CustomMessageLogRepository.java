package com.dodo.smartsafereturn.messagelog.repository;

import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;
import com.dodo.smartsafereturn.messagelog.entity.MessageLog;

import java.util.List;

public interface CustomMessageLogRepository {

    List<MessageLogResponseDto> getMessagesBySafeRouteId(Long safeRouteId);
    List<MessageLogResponseDto> getMessagesByMemberNumber(Long messageLogId);
    List<MessageLogResponseDto> getMessages();
}
