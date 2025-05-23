package com.dodo.smartsafereturn.member.controller;

import com.dodo.smartsafereturn.member.dto.*;
import com.dodo.smartsafereturn.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/{memberNumber}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateMember(@Validated @ModelAttribute MemberUpdateDto dto) {
        memberService.update(dto);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @Operation(
            summary = "회원 탈퇴",
            description = "path variable 로 member_number 회원 PK 값을 가져와서 탈퇴 처리 (is_delete 플래그 변경)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 탈퇴 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원 탈퇴 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{memberNumber}")
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "회원 PK (member_number)", required = true, example = "1")
            @PathVariable Long memberNumber
    ) {
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
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{memberNumber}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long memberNumber) {
        MemberResponseDto member = memberService.getMember(memberNumber);
        return ResponseEntity.ok(member);
    }

    // 회원 리스트 조회
    @Operation(
            summary = "회원 전체 리스트 조회 (활성화된 회원 List)",
            description = "회원 데이터 List 를 조회합니다. 제한없이 회원 정보를 볼 수 있기 때문에 어드민만 접근 가능하도록 설정해야 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 리스트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MemberResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "회원 정보 응답 예시",
                                                    summary = "성공 시, 회원 정보 응답 dto JSON 정보",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"memberNumber\": 1,\n" +
                                                            "    \"id\": \"ExampleMan\",\n" +
                                                            "    \"phone\": \"01012345678\",\n" +
                                                            "    \"createdDate\": \"2025-04-03T01:10:01.759Z\",\n" +
                                                            "    \"profile\": \"https://storage.googleapis.com/safe-return-bucket/member/profile/[example.png]\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"memberNumber\": 2,\n" +
                                                            "    \"id\": \"ExampleWoman\",\n" +
                                                            "    \"phone\": \"01087654321\",\n" +
                                                            "    \"createdDate\": \"2025-04-02T23:15:42.123Z\",\n" +
                                                            "    \"profile\": \"https://storage.googleapis.com/safe-return-bucket/member/profile/[example2.png]\"\n" +
                                                            "  }\n" +
                                                            "]"
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
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }

    // 회원 가입 - 아이디 중복 체크 엔드포인트
    @Operation(
            summary = "회원 아이디 중복 체크",
            description = "회원 가입 시 아이디 중복 여부를 확인합니다",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "중복 체크할 회원 아이디",
                            required = true,
                            example = "test",
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "중복 체크 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "중복 체크 결과",
                                                    summary = "true: 중복된 아이디, false: 사용 가능한 아이디",
                                                    value = "false"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @GetMapping("/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam("id") String id) {
        boolean isDuplicate = memberService.checkDuplicate(id);
        return ResponseEntity.ok(isDuplicate);
    }

    // 비밀 번호 체크 엔드포인트
    @Operation(
            summary = "회원 비밀번호 유효성 체크",
            description = "회원 비밀번호 변경 또는 중요 기능 접근 시 현재 비밀번호의 유효성을 확인합니다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "비밀번호 체크 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PasswordCheckDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "비밀번호 체크 요청 예시",
                                            summary = "아이디와 비밀번호 입력 예시",
                                            value = "{\n" +
                                                    "  \"id\": \"ExampleMan\",\n" +
                                                    "  \"password\": \"!test1234\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비밀번호 체크 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "비밀번호 체크 결과",
                                                    summary = "true: 유효한 비밀번호, false: 유효하지 않은 비밀번호",
                                                    value = "true"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "유효하지 않은 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @PostMapping("/{memberNumber}/password-check")
    public ResponseEntity<Boolean> checkPassword(@RequestBody @Validated PasswordCheckDto dto) {
        boolean isValid = memberService.checkPassword(dto);
        return ResponseEntity.ok(isValid);
    }
}
