package com.dodo.smartsafereturn.member.controller;

import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 API CRUD 및 마이페이지 관련 기능 구현")
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 - MultipartFile 처리를 위해 consumes 설정 / 파일 업로드 -> multipart/form-data
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> joinMember(@Validated @ModelAttribute MemberJoinDto dto) {
        memberService.join(dto);
        return ResponseEntity.ok().build();
    }

    // 회원 수정 (비밀번호 or 휴대폰 번호 or 프로필 이미지) - MultipartFile 처리를 위해 consumes 설정 / 파일 업로드 -> multipart/form-data
    @PutMapping(value = "/{memberNumber}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateMember(@Validated @ModelAttribute MemberUpdateDto dto) {
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
    @Operation(
            summary = "마이페이지용 회원 한 건 조회",
            description = "member_number PK 값으로 회원 한 명의 데이터를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 한 건 조회 성공",
                            content = @Content(schema = @Schema(implementation = MemberResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "데이터를 찾을 수 없음")
            }
    )
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
