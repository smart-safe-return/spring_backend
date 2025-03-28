package com.dodo.smartsafereturn.member.service;

import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;

import java.util.List;

public interface MemberService {

    // 회원 가입
    void join(MemberJoinDto memberJoinDto);

    // 회원 수정 (비밀번호 or 휴대폰 번호)
    void update(MemberUpdateDto memberUpdateDto);

    // 회원 탈퇴
    void delete(Long memberNumber);

    // 회원 한건 조회 (마이페이지 용)
    MemberResponseDto getMember(Long memberNumber);

    // 회원 리스트 조회
    List<MemberResponseDto> getMembers();

}
