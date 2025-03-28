package com.dodo.smartsafereturn.sosmessage.service;

import com.dodo.smartsafereturn.sosmessage.dto.SosMessageCreateDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageResponseDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageUpdateDto;

public interface SosMessageService {

    // sos 메시지 등록
    void createSosMessage(SosMessageCreateDto createDto);
    // sos 메시지 수정
    void updateSosMessage(SosMessageUpdateDto updateDto);
    // sos 메시지 삭제
    void deleteSosMessage(Long sosMessageId);
    // sos 메시지 조회 (한 회원에 등록된 메시지)
    SosMessageResponseDto getSosMessage(Long memberNumber);

}
