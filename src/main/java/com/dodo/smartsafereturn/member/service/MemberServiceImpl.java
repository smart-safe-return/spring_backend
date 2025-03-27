package com.dodo.smartsafereturn.member.service;

import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void join(MemberJoinDto memberJoinDto) {
        // todo member서비스 완성하기
    }

    @Transactional
    @Override
    public void update(MemberUpdateDto memberUpdateDto) {

    }

    @Transactional
    @Override
    public void delete(Long memberNumber) {

    }

    @Override
    public MemberResponseDto getMember(Long memberNumber) {
        return null;
    }

    @Override
    public List<MemberResponseDto> getMembers() {
        return List.of();
    }
}
