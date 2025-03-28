package com.dodo.smartsafereturn.sosmessage.service;

import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageCreateDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageResponseDto;
import com.dodo.smartsafereturn.sosmessage.dto.SosMessageUpdateDto;
import com.dodo.smartsafereturn.sosmessage.entity.SosMessage;
import com.dodo.smartsafereturn.sosmessage.repository.SosMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SosMessageServiceImpl implements SosMessageService {

    private final SosMessageRepository sosMessageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void createSosMessage(SosMessageCreateDto createDto) {

        // 연결 member 찾기
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(createDto.getMemberNumber())
                .orElseThrow(() -> new RuntimeException("[SosMessageService] createSosMessage() : 존재하지 않는 회원"));

        // 회원이 이미 메시지를 만들었는 지 검증
        // 물리적으로 DB에 존재하는지 검색
        Optional<SosMessage> existingMessage = sosMessageRepository.findByMember_memberNumber(createDto.getMemberNumber());

        SosMessage sosMessage;
        if (existingMessage .isPresent()) {
            sosMessage = existingMessage .get();

            // is_deleted flag 가 true 인지 확인
            if (sosMessage.getIsDeleted()) {
                // true 인놈에 덮어쓰고 플래그 false 로 바꾸기
                sosMessage.changeDeleteFlag();
                sosMessage.update(createDto.getContent());
            } else {
                // 물리적 / 논리적으로 존재하는데도 등록하려 했으므로 예외 반환
                throw new RuntimeException("duplicate sosMessage");
            }
        } else {
            // 새 메시지 만들어 보내기 -> 물리적으로 존재하지 않음
            sosMessageRepository.save(
                    SosMessage.builder()
                            .content(createDto.getContent())
                            .member(member)
                            .build());
        }
    }

    @Transactional
    @Override
    public void updateSosMessage(SosMessageUpdateDto updateDto) {
        SosMessage sosMessage = sosMessageRepository.findByIdAndIsDeletedIsFalse(updateDto.getSosMessageId())
                .orElseThrow(() -> new RuntimeException("[SosMessageService] createSosMessage() : 존재하지 않는 메시지"));

        sosMessage.update(updateDto.getContent());

    }

    @Transactional
    @Override
    public void deleteSosMessage(Long sosMessageId) {

        SosMessage sosMessage = sosMessageRepository.findByIdAndIsDeletedIsFalse(sosMessageId)
                .orElseThrow(() -> new RuntimeException("[SosMessageService] createSosMessage() : 존재하지 않는 메시지"));

        // 삭제 플래그가 false 일때만 작동
        if (!sosMessage.getIsDeleted()) {
            sosMessage.changeDeleteFlag();
        } else {
            throw new RuntimeException("SosMessageService : 이미 삭제되어 있음");
        }
    }

    @Override
    public SosMessageResponseDto getSosMessage(Long memberNumber) {

        SosMessage sosMessage = sosMessageRepository.findMemberSosMessage(memberNumber)
                .orElseThrow(() -> new RuntimeException("[SosMessageService] createSosMessage() : 존재하지 않는 메시지"));

        return SosMessageResponseDto.builder()
                .sosMessageId(sosMessage.getId())
                .content(sosMessage.getContent())
                .createdDate(sosMessage.getCreatedDate())
                .build();
    }
}
