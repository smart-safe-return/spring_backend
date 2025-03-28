package com.dodo.smartsafereturn.member.service;

import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void join(MemberJoinDto memberJoinDto) {

        // 이미 회원 가입된 회원인지 확인 (ID값)
        boolean isExist = memberRepository.existsByIdAndIsDeletedIsFalse(memberJoinDto.getId());
        if (isExist) {
            log.info("[MemberService] Join() 예외 발생 : [회원 가입 로직] 이미 존재하는 회원입니다");
            throw  new RuntimeException("[회원 가입 로직] 이미 존재하는 회원입니다");
        }
        
        // 회원 가입
        memberRepository.save(
                Member.builder()
                        .id(memberJoinDto.getId())
                        .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                        .phone(memberJoinDto.getPhone())
                        .build()
        );
    }

    @Transactional
    @Override
    public void update(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(memberUpdateDto.getMemberNumber())
                .orElseThrow(() -> new RuntimeException("[MemberService] update() : 존재하지 않는 회원"));
        
        // dirty checking 활용
        member.updateMember(memberUpdateDto);
    }

    @Transactional
    @Override
    public void delete(Long memberNumber) {

        // 존재 여부 체크
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(memberNumber)
                .orElseThrow(() -> new RuntimeException("[MemberService] delete() : 존재하지 않는 회원"));
        // is_Deleted 플래그 변경 -> dirty checking
        if (!member.getIsDeleted()) {
            member.changeDeleteFlag();
        }
    }

    @Override
    public MemberResponseDto getMember(Long memberNumber) {

        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(memberNumber)
                .orElseThrow(() -> new RuntimeException("[MemberService] getMember() : 존재하지 않는 회원"));

        return MemberResponseDto.builder()
                .memberNumber(memberNumber)
                .id(member.getId())
                .phone(member.getPhone())
                .createdDate(member.getCreatedDate())
                .build();
    }

    @Override
    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAllByIsDeletedIsFalse()
                .stream()
                .map(m -> MemberResponseDto.builder()
                        .memberNumber(m.getMemberNumber())
                        .id(m.getId())
                        .phone(m.getPhone())
                        .createdDate(m.getCreatedDate())
                        .build())
                .toList();
    }
}
