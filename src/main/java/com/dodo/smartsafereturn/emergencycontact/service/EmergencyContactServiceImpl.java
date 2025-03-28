package com.dodo.smartsafereturn.emergencycontact.service;

import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactCreateDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactResponseDto;
import com.dodo.smartsafereturn.emergencycontact.dto.EmergencyContactUpdateDto;
import com.dodo.smartsafereturn.emergencycontact.entity.EmergencyContact;
import com.dodo.smartsafereturn.emergencycontact.repository.EmergencyContactRepository;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void create(EmergencyContactCreateDto createDto) {

        // 이미 회원이 비상연락망으로 등록한 사람인지 검증
        boolean isExist = emergencyContactRepository.existsByMember_memberNumberAndPhone(createDto.getMemberNumber(), createDto.getPhone());
        if (isExist) {
            throw new RuntimeException("[EmergencyContactService] create() : 이미 회원이 등록한 비상연락망");
        }

        // 연관관계를 위해 member 가져오기
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(createDto.getMemberNumber())
                .orElseThrow(() -> new RuntimeException("[EmergencyContactService] create() : 존재하지 않는 회원"));

        // 비상연락망 등록
        emergencyContactRepository.save(
                EmergencyContact.builder()
                        .name(createDto.getName())
                        .phone(createDto.getPhone())
                        .member(member)
                        .build()
        );

    }

    @Transactional
    @Override
    public void update(EmergencyContactUpdateDto updateDto) {

        EmergencyContact emergencyContact = emergencyContactRepository.findById(updateDto.getEmergencyContactId())
                .orElseThrow(() -> new RuntimeException("[EmergencyContactService] update() : 존재하지 않는 비상연락망"));

        emergencyContact.update(updateDto);
    }

    @Transactional
    @Override
    public void delete(Long emergencyContactId) {
        // 물리 삭제
        emergencyContactRepository.deleteById(emergencyContactId);
    }

    @Override
    public List<EmergencyContactResponseDto> getMemberContacts(Long memberNumber) {

        List<EmergencyContact> memberContacts = emergencyContactRepository.findMemberContacts(memberNumber);

        return memberContacts
                .stream()
                .map(ec ->
                                EmergencyContactResponseDto.builder()
                                        .emergencyContactId(ec.getId())
                                        .name(ec.getName())
                                        .phone(ec.getPhone())
                                        .createdDate(ec.getCreatedDate())
                                        .build()
                )
                .toList();
    }
}
