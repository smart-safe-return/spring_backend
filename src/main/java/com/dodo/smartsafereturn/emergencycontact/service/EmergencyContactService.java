package com.dodo.smartsafereturn.emergencycontact.service;

import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactCreateDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactResponseDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactUpdateDto;

import java.util.List;

public interface EmergencyContactService {

    // sos 비상연락망 등록
    void create(EmergencyContactCreateDto createDto);
    // sos 비상연락망 수정
    void update(EmergencyContactUpdateDto updateDto);
    // sos 비상연락망 삭제
    void delete(Long emergencyContactId);
    // sos 비상연락망 조회 (한 회원에 등록된 번호들)
    List<EmergencyContactResponseDto> getMemberContacts(Long memberNumber);
}
