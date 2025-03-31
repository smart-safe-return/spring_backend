package com.dodo.smartsafereturn.messagelog.service;

import com.dodo.smartsafereturn.messagelog.dto.MessageLogCreateDto;
import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;
import com.dodo.smartsafereturn.messagelog.entity.MessageLog;
import com.dodo.smartsafereturn.messagelog.repository.MessageLogRepository;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import com.dodo.smartsafereturn.safeRoute.repository.SafeRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageLogServiceImpl implements MessageLogService {

    private final MessageLogRepository messageLogRepository;
    private final SafeRouteRepository safeRouteRepository;

    @Transactional
    @Override
    public void save(MessageLogCreateDto dto) {
        // 연관 귀가 루트 있는지 여부 확인
        SafeRoute safeRoute = safeRouteRepository.findById(dto.getSafeRouteId())
                .orElseThrow(() -> new RuntimeException("[MessageLogService] save() : 존재 하지 않는 귀가루트"));

        // todo 추후 coolsms 기능 추가

        MessageLog savedMessageLog = messageLogRepository.save(
                MessageLog.builder()
                        .message(dto.getMessage())
                        .safeRoute(safeRoute)
                        .build()
        );

        // 양방향 연관관계 설정
        safeRoute.addMessageLog(savedMessageLog);
    }

    @Transactional
    @Override
    public void delete(Long messageLogId) {
        // 실제하는 메시지인지 확인
        messageLogRepository.findById(messageLogId)
                        .orElseThrow(() -> new RuntimeException("실제 존재하지 않는 sms 메시지 입니다"));

        messageLogRepository.deleteById(messageLogId);
    }

    @Override
    public MessageLogResponseDto getMessage(Long messageLogId) {

        MessageLog messageLog = messageLogRepository.findById(messageLogId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 sms 메시지 입니다"));

        return MessageLogResponseDto.builder()
                .messageLogId(messageLog.getId())
                .message(messageLog.getMessage())
                .createDate(messageLog.getCreatedDate())
                .build();
    }

    @Override
    public List<MessageLogResponseDto> getMessagesBySafeRouteId(Long safeRouteId) {
        return messageLogRepository.getMessagesBySafeRouteId(safeRouteId);
    }

    @Override
    public List<MessageLogResponseDto> getMessagesByMemberNumber(Long memberNumber) {
        return messageLogRepository.getMessagesByMemberNumber(memberNumber);
    }

    @Override
    public List<MessageLogResponseDto> getMessages() {
        return messageLogRepository.getMessages();
    }
}
