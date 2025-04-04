package com.dodo.smartsafereturn.question.controller;

import com.dodo.smartsafereturn.auth.dto.Role;
import com.dodo.smartsafereturn.question.dto.*;
import com.dodo.smartsafereturn.question.entity.QuestionStatus;
import com.dodo.smartsafereturn.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
@Tag(name = "문의 글 관리 API", description = "문의 글 CRUD 및 상태 관리 API")
public class QuestionController {

    private final QuestionService questionService;

    // 문의 글 등록 -> 등록된 question_id 값 줘서 바로 가게 해주기
    @Operation(
            summary = "문의 글 등록",
            description = "새로운 문의 글을 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 문의 글 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "문의 글 등록 요청 예시",
                                            summary = "문의 글 등록에 필요한 데이터",
                                            value = """
                                                    {
                                                      "member_number": 1,
                                                      "title": "앱 사용 중 문제가 발생했습니다",
                                                      "content": "안전 귀가 경로 설정 중에 오류가 발생합니다. 도움이 필요합니다.",
                                                      "category": "APP_ERROR"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 등록 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Long.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "등록 성공한 문의 글 ID",
                                                    summary = "등록된 문의 글의 ID 값",
                                                    value = "1"
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
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("")
    public ResponseEntity<Long> create(@Validated @RequestBody QuestionCreateDto dto) {
        Long result = questionService.create(dto);
        return ResponseEntity.ok(result);
    }

    // 문의 글 수정
    @Operation(
            summary = "문의 글 수정",
            description = "기존 문의 글의 내용을 수정합니다.",
            parameters = {
                    @Parameter(name = "questionId", description = "수정할 문의 글 ID", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 문의 글 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "문의 글 수정 요청 예시",
                                            summary = "문의 글 수정에 필요한 데이터",
                                            value = """
                                                    {
                                                      "question_id": 1,
                                                      "title": "문의 제목 수정합니다",
                                                      "content": "문의 내용을 수정합니다. 더 자세한 설명을 추가했습니다.",
                                                      "category": "SERVICE_INQUIRY"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "문의 글을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{questionId}")
    public ResponseEntity<Void> update(@Validated @RequestBody QuestionUpdateDto dto) {
        questionService.update(dto);
        return ResponseEntity.ok().build();
    }

    // 문의 글 삭제 is_delete 플래그 변경
    @Operation(
            summary = "문의 글 삭제",
            description = "문의 글을 삭제합니다(is_delete 플래그 변경).",
            parameters = {
                    @Parameter(name = "questionId", description = "삭제할 문의 글 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "문의 글을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> delete(@PathVariable Long questionId) {
        questionService.delete(questionId);
        return ResponseEntity.ok().build();
    }

    // 문의 글 한 건 조회
    @Operation(
            summary = "문의 글 상세 조회",
            description = "특정 문의 글의 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "questionId", description = "조회할 문의 글 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "문의 글 상세 조회 응답 예시",
                                                    summary = "문의 글 상세 정보",
                                                    value = """
                                                            {
                                                              "question_id": 1,
                                                              "member_id": "대상혁",
                                                              "category": "APP_ERROR",
                                                              "title": "앱 사용 중 문제가 발생했습니다",
                                                              "content": "안전 귀가 경로 설정 중에 오류가 발생합니다. 도움이 필요합니다.",
                                                              "create_date": "2025-04-03T10:15:30.123Z",
                                                              "modified_date": "2025-04-03T10:15:30.123Z",
                                                              "status": "IN_PROGRESS"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "문의 글을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("{questionId}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getOne(questionId));
    }


    // 문의 글 조회 - 회원 조건
    @Operation(
            summary = "회원별 문의 글 목록 조회",
            description = "특정 회원이 작성한 모든 문의 글 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "memberNumber", description = "회원 번호", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원별 문의 글 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "회원별 문의 글 목록 응답 예시",
                                                    summary = "회원이 작성한 문의 글 목록",
                                                    value = """
                                                            [
                                                              {
                                                                "question_id": 1,
                                                                "member_id": "왕눈이",
                                                                "title": "앱 사용 중 문제가 발생했습니다",
                                                                "content": "안전 귀가 경로 설정 중에 오류가 발생합니다.",
                                                                "category": "APP_ERROR",
                                                                "status": "COMPLETED",
                                                                "create_date": "2025-04-03T10:15:30.123Z",
                                                                "modified_date": "2025-04-03T10:15:30.123Z"
                                                              },
                                                              {
                                                                "question_id": 2,
                                                                "member_id": "백강혁",
                                                                "title": "서비스 이용 문의",
                                                                "content": "서비스 이용 방법에 대해 문의드립니다.",
                                                                "category": "SERVICE_INQUIRY",
                                                                "status": "CANCELLED",
                                                                "create_date": "2025-04-02T14:30:45.789Z",
                                                                "modified_date": "2025-04-02T14:30:45.789Z"
                                                              }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionByMemberNumber(@PathVariable Long memberNumber) {
        return ResponseEntity.ok(questionService.getOneByMember(memberNumber));
    }

    // 문의 글 전체 조회
    @Operation(
            summary = "문의 글 전체 조회 (관리자용)",
            description = "모든 문의 글 목록을 조회합니다. 관리자만 접근 가능합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 전체 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionResponseListDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "문의 글 전체 조회 (관리자용) 응답 예시",
                                                    summary = "모든 문의 글 목록 -> 관리자만 접근 가능",
                                                    value = """
                                                            [
                                                              {
                                                                "question_id": 1,
                                                                "member_id": "왕눈이",
                                                                "title": "앱 사용 중 문제가 발생했습니다",
                                                                "category": "APP_ERROR",
                                                                "status": "COMPLETED",
                                                                "create_date": "2025-04-03T10:15:30.123Z",
                                                                "modified_date": "2025-04-03T10:15:30.123Z"
                                                              },
                                                              {
                                                                "question_id": 2,
                                                                "member_id": "백강혁",
                                                                "title": "서비스 이용 문의",
                                                                "category": "SERVICE_INQUIRY",
                                                                "status": "CANCELLED",
                                                                "create_date": "2025-04-02T14:30:45.789Z",
                                                                "modified_date": "2025-04-02T14:30:45.789Z"
                                                              }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "접근 권한 없음 (관리자가 아님)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "\"관리자만 접근 가능\""
                                            )
                                    }
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    public ResponseEntity<?> getAllQuestions() {
        // admin 만 접근하도록 권한 제어
        if (!SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority().equals(Role.ROLE_ADMIN.getValue())) {
            return new ResponseEntity<>("관리자만 접근 가능", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(questionService.getList());
    }

    /**
     * 문의 글 검색 조건 별 조회 -> 쿼리스트링 활용
     * - title, content, memberId, category (검색조건) > 쿼리스트링은 스네이크를 카멜케이스 바꾸는거 설정 너무 귀찮으니까 그냥 저걸로 받으셈
     * - size, sort, desc(or asc) (페이징 조건)
     * 필요한 매개값들 쿼리스트링으로 검색해서 프론트에서 요청
     */
    @Operation(
            summary = "문의 글 리스트 조회 (페이징)",
            description = "검색 조건과 페이징 조건을 쿼리스트링으로 받아 문의 글 리스트를 조회합니다" +
                    " 단 size, sort, direction 은 기본 값 설정되있으므로 특별히 변수로 안보내도 됨",
            parameters = {
                    @Parameter(name = "title", description = "제목 검색어", example = "오류"),
                    @Parameter(name = "content", description = "내용 검색어", example = "앱"),
                    @Parameter(name = "memberId", description = "회원 ID 검색", example = "user123"),
                    @Parameter(name = "category", description = "카테고리 검색", example = "APP_ERROR"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 크기", example = "10"),
                    @Parameter(name = "sort", description = "정렬 기준 필드", example = "createdDate"),
                    @Parameter(name = "direction", description = "정렬 방향 (ASC/DESC)", example = "desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 리스트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "문의 글 리스트 페이징 응답 예시",
                                                    summary = "페이징된 문의 글 리스트 조회 결과",
                                                    value = """
                                                            {
                                                              "content": [
                                                                {
                                                                  "question_id": 1,
                                                                  "member_id": "faker",
                                                                  "title": "앱 사용 중 문제가 발생했습니다",
                                                                  "category": "APP_ERROR",
                                                                  "status": "PENDING",
                                                                  "create_date": "2025-04-03T10:15:30.123Z",
                                                                  "modified_date": "2025-04-03T10:15:30.123Z"
                                                                },
                                                                {
                                                                  "question_id": 2,
                                                                  "member_id": "faker",
                                                                  "title": "서비스 이용 문의",
                                                                  "category": "SERVICE_INQUIRY",
                                                                  "status": "COMPLETED",
                                                                  "create_date": "2025-04-02T14:30:45.789Z",
                                                                  "modified_date": "2025-04-02T14:30:45.789Z"
                                                                }
                                                              ],
                                                              "pageable": {
                                                                "pageNumber": 0,
                                                                "pageSize": 10,
                                                                "sort": {
                                                                  "sorted": true,
                                                                  "unsorted": false,
                                                                  "empty": false
                                                                },
                                                                "offset": 0,
                                                                "paged": true,
                                                                "unpaged": false
                                                              },
                                                              "totalPages": 5,
                                                              "totalElements": 42,
                                                              "last": false,
                                                              "size": 10,
                                                              "number": 0,
                                                              "sort": {
                                                                "sorted": true,
                                                                "unsorted": false,
                                                                "empty": false
                                                              },
                                                              "numberOfElements": 10,
                                                              "first": true,
                                                              "empty": false
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "조회 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<Page<QuestionResponseListDto>> searchPagingQuestions(
            @ModelAttribute QuestionSearchCondition condition,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<QuestionResponseListDto> results = questionService.getListByCondition(condition, pageable);
        return ResponseEntity.ok(results);
    }

    // 상태 변경
    @Operation(
            summary = "문의 글 상태 변경",
            description = "문의 글의 상태를 변경합니다(대기중, 답변완료 등).",
            parameters = {
                    @Parameter(name = "questionId", description = "상태를 변경할 문의 글 ID", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "변경할 상태 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionUpdateStateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "상태 변경 요청 예시",
                                            summary = "문의 글 상태 변경 데이터",
                                            value = """
                                                    {
                                                      "status": "COMPLETED"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "문의 글 상태 변경 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 또는 유효하지 않은 상태값",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "문의 글을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{questionId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long questionId, @Validated @RequestBody QuestionUpdateStateDto dto) {
        questionService.updateStatus(questionId, dto.getStatus());
        return ResponseEntity.ok().build();
    }
}