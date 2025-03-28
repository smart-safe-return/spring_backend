package com.dodo.smartsafereturn.member.controller;

import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping("")
    public ResponseEntity<Void> joinMember(@RequestBody MemberJoinDto dto) {
        memberService.join(dto);
        return ResponseEntity.ok().build();
    }

    // 회원 수정 (비밀번호 or 휴대폰 번호)
    @PutMapping("/{memberNumber}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberNumber, @RequestBody MemberUpdateDto dto) {
        memberService.update(dto);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberNumber}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberNumber) {
        memberService.delete(memberNumber);
        return ResponseEntity.ok().build();
    }

    // 회원 한건 조회 (마이페이지 용)
    @GetMapping("/{memberNumber}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberNumber) {
        MemberResponseDto member = memberService.getMember(memberNumber);
        return ResponseEntity.ok(member);
    }

    // 회원 리스트 조회
    @GetMapping("")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }
}
