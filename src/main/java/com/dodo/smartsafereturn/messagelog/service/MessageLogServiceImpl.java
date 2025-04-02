package com.dodo.smartsafereturn.messagelog.service;

import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactResponseDto;
import com.dodo.smartsafereturn.emergencycontact.service.EmergencyContactService;
import com.dodo.smartsafereturn.messagelog.dto.MessageLogCreateDto;
import com.dodo.smartsafereturn.messagelog.dto.MessageLogResponseDto;
import com.dodo.smartsafereturn.messagelog.entity.MessageLog;
import com.dodo.smartsafereturn.messagelog.repository.MessageLogRepository;
import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import com.dodo.smartsafereturn.safeRoute.repository.SafeRouteRepository;
import com.dodo.smartsafereturn.sms.service.SmsService;
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
    private final SmsService smsService; // sms 서비스
    private final EmergencyContactService emergencyContactService;

    /**
     * Cool SMS 실제 메시지 보내기 -> 메시지 로그 등록
     * @param dto
     */
    @Transactional
    @Override
    public void save(MessageLogCreateDto dto) {
        // 연관 귀가 루트 있는지 여부 확인
        SafeRoute safeRoute = safeRouteRepository.findById(dto.getSafeRouteId())
                .orElseThrow(() -> new RuntimeException("[MessageLogService] save() : 존재 하지 않는 귀가루트"));

        // memberNumber 로 비상연락망 검색 후, 연락망만큼 sms 보내기
        List<EmergencyContactResponseDto> toList = emergencyContactService.getMemberContacts(dto.getNumberNumber());

        // cool sms 전송
        if (toList.isEmpty()) {
            throw new RuntimeException("[MessageLogService] coolSms 전송 실패 -> 비상연락망 등록한 사람이 없음");
        } else if (toList.size() == 1) {
            smsService.sendSms(toList.getFirst().getPhone(), dto.getMessage());
        } else {
            // dto 에서 보낼 연락처들 뽑아 내기
            List<String> toNumbers = toList.stream()
                    .map(EmergencyContactResponseDto::getPhone)
                    .toList();
            
            smsService.sendSmsToMany(toNumbers, dto.getMessage());
        }

        MessageLog savedMessageLog = messageLogRepository.save(
                MessageLog.builder()
                        .message(dto.getMessage())
                        .safeRoute(safeRoute)
                        .location(dto.getLocation())
                        .build()
        );

        // 양방향 연관관계 설정
        safeRoute.addMessageLog(savedMessageLog);

        // 혹시 모를 SafeRoute State 실패로 변경 (메시지는 FAIL 일시만 전송됨)
        safeRoute.changeIsSuccess(RouteState.FAILED);
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
                .location(messageLog.getLocation())
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
