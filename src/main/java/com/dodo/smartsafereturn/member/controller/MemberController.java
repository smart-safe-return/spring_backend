package com.dodo.smartsafereturn.member.controller;

import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @Operation(
            summary = "회원 가입",
            description = "multipart/form-data 형식으로 MemberJoinDto 를 받아와서 회원 가입 요청 진행",
            requestBody = @RequestBody(
                    description = "등록할 회원 정보",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = MemberJoinDto.class),
                            encoding = {
                                    @Encoding(name = "file", contentType = "image/jpeg, image/png")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 가입 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원가입 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "id", description = "사용자 아이디", example = "test", required = true),
            @Parameter(name = "password", description = "비밀번호", example = "!test1234", required = true),
            @Parameter(name = "phone", description = "휴대폰 번호", example = "01012345678", required = true),
            @Parameter(name = "file", description = "프로필 이미지", schema = @Schema(type = "string", format = "binary"))
    })
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> joinMember(@Validated @ModelAttribute MemberJoinDto dto) {
        memberService.join(dto);
        return ResponseEntity.ok().build();
    }

    // 회원 수정 (비밀번호 or 휴대폰 번호 or 프로필 이미지) - MultipartFile 처리를 위해 consumes 설정 / 파일 업로드 -> multipart/form-data
    @Operation(
            summary = "회원 수정",
            description = "multipart/form-data 형식으로 MemberUpdateDto 를 받아와서 회원 가입 요청 진행",
            requestBody = @RequestBody(
                    description = "수정할 회원 정보",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = MemberUpdateDto.class),
                            encoding = {
                                    @Encoding(name = "file", contentType = "image/jpeg, image/png")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 수정 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원 수정 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "memberNumber", description = "회원 PK값", example = "1", required = true),
            @Parameter(name = "password", description = "비밀번호", example = "!test1234"),
            @Parameter(name = "phone", description = "휴대폰 번호", example = "01012345678"),
            @Parameter(name = "file", description = "프로필 이미지", schema = @Schema(type = "string", format = "binary"))
    })
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
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MemberResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "회원 정보 응답 예시",
                                                    summary = "성공 시, 회원 정보 응답 dto JSON 정보",
                                                    value = "{\n" +
                                                            "  \"memberNumber\": 1,\n" +
                                                            "  \"id\": \"ExampleMan\",\n" +
                                                            "  \"phone\": \"01012345678\",\n" +
                                                            "  \"createdDate\": \"2025-04-03T01:10:01.759Z\",\n" +
                                                            "  \"profile\": \"https://storage.googleapis.com/safe-return-bucket/member/profile/[example.png]\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "데이터를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
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
