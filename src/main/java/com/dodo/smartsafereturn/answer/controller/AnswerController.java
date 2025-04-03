package com.dodo.smartsafereturn.answer.controller;

import com.dodo.smartsafereturn.admin.dto.AdminCreateDto;
import com.dodo.smartsafereturn.answer.dto.AnswerCreateDto;
import com.dodo.smartsafereturn.answer.dto.AnswerResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerSearchCondition;
import com.dodo.smartsafereturn.answer.dto.AnswerUpdateDto;
import com.dodo.smartsafereturn.answer.service.AnswerService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
@Tag(name = "답변하기 API", description = "답변 글 API CRUD 구현")
public class AnswerController {

    private final AnswerService answerService;

    // 답변 글 등록 (어드민)
    @Operation(
            summary = "답변 글 등록",
            description = "json 형식으로 AnswerCreateDto 를 받아와서 답변 글 등록 요청 진행",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 답변 글 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnswerCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "답변 글 등록 요청 Dto 예시",
                                            summary = "답변 글 등록 요청을 보낼 때 body 에 보내야 할 것들 입니다",
                                            value = "{\n" +
                                                    "  \"admin_number\": \"1\",\n" +
                                                    "  \"question_id\": \"1\",\n" +
                                                    "  \"title\": \"답변글입니다\",\n" +
                                                    "  \"content\": \"문의 답변글 예시입니다\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 글 등록 성공",
                            content = @Content(schema = @Schema(implementation = Long.class),
                            examples = {
                                    @ExampleObject(
                                            name = "등록 성공한 답변 글 pk값",
                                            summary = "등록 성공한 답변 글 pk값을 가지고 프론트에서 바로 글 조회 요청 or 상세 페이지 갈 선택지를 가질 수 있음",
                                            value = "1"
                                    )
                            })
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "답변 글 등록 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("")
    public ResponseEntity<?> createAnswer(@Validated @RequestBody AnswerCreateDto answerCreateDto) {
        Long answerId = answerService.create(answerCreateDto);
        return ResponseEntity.ok().body(answerId);
    }

    // 답변 글 수정 (어드민)
    @Operation(
            summary = "답변 글 수정",
            description = "json 형식으로 AnswerUpdateDto 를 받아와서 답변 글 수정 요청 진행",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 답변 글 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnswerUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "답변 글 수정 요청 Dto 예시",
                                            summary = "답변 글 수정 요청을 보낼 때 body 에 보내야 할 것들 입니다",
                                            value = "{\n" +
                                                    "  \"answer_id\": \"1\",\n" +
                                                    "  \"title\": \"답변글 수정 입니다\",\n" +
                                                    "  \"content\": \"문의 답변글 수정 예시입니다\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 글 수정 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "답변 글 등록 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(@Validated @RequestBody AnswerUpdateDto answerUpdateDto) {
        answerService.update(answerUpdateDto);
        return ResponseEntity.ok().build();
    }

    // 답변 글 삭제 (어드민)
    @Operation(
            summary = "답변 글 삭제",
            description = "path variable 형식으로 answer_id 를 받아와서 답변 글 삭제 요청 진행",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "답변 글 삭제 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswer(
            @Parameter(description = "답변 글 PK (answer_id)", required = true, example = "1")
            @PathVariable Long answerId) {
        answerService.delete(answerId);
        return ResponseEntity.ok().build();
    }

    // 답변 글 상세 정보 한건 조회 (어드민)
    @Operation(
            summary = "답변 글 상세 정보 한건 조회",
            description = "path variable 형식으로 answer_id 를 받아와서 답변 글 한건 조회 요청 진행",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 글 한 건 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AnswerResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "답변 글 한건 조회 성공 응답 Dto 예시",
                                                    summary = "답변 글 한건 조회 성공 응답을 보낼 때 body 에 보내야 할 것들 입니다",
                                                    value = "{\n" +
                                                            "  \"answer_id\": \"1\",\n" +
                                                            "  \"admin_id\": \"1\",\n" +
                                                            "  \"question_id\": \"1\",\n" +
                                                            "  \"question_title\": \"문의 글 1\",\n" +
                                                            "  \"title\": \"답변글 제목 예시입니다\",\n" +
                                                            "  \"content\": \"문의 답변글 내용 예시입니다\",\n" +
                                                            "  \"deleted\": \"false\",\n" +
                                                            "  \"create_date\": \"2025-04-03T02:10:01.759Z\",\n" +
                                                            "  \"modified_date\": \"2025-04-03T02:10:01.759Z\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "답변 글 한 건 조회 실패",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{answerId}")
    public ResponseEntity<?> getAnswer(
            @Parameter(description = "답변 글 PK (answer_id)", required = true, example = "1")
            @PathVariable Long answerId) {
        return ResponseEntity.ok(answerService.getOne(answerId));
    }

    /**
     * 답변 글 리스트 조회 페이징 (문의자, 카테고리, 날짜 검색조건) -> 쿼리스트링 활용
     * - title, content, adminId (검색조건)
     * - size, sort, desc(or asc) (페이징 조건)
     * 필요한 매개값들 쿼리스트링으로 검색해서 프론트에서 요청
     */
    @Operation(
            summary = "답변 글 리스트 조회 (페이징)",
            description = "검색 조건과 페이징 조건을 쿼리스트링으로 받아 답변 글 리스트를 조회합니다",
            parameters = {
                    @Parameter(name = "title", description = "제목 검색어", example = "환불"),
                    @Parameter(name = "content", description = "내용 검색어", example = "처리"),
                    @Parameter(name = "adminId", description = "관리자 ID 검색", example = "admin1"),
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
                    @Parameter(name = "size", description = "한 페이지 크기", example = "10"),
                    @Parameter(name = "sort", description = "정렬 기준 필드", example = "createdDate"),
                    @Parameter(name = "direction", description = "정렬 방향 (ASC/DESC)", example = "desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 글 리스트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "답변 글 리스트 조회 성공 응답 예시",
                                                    summary = "페이징된 답변 글 리스트 조회 결과",
                                                    value = "{\n" +
                                                            "  \"content\": [\n" +
                                                            "    {\n" +
                                                            "      \"answer_id\": \"1\",\n" +
                                                            "      \"admin_id\": \"1\",\n" +
                                                            "      \"question_id\": \"1\",\n" +
                                                            "      \"question_title\": \"문의 글 1\",\n" +
                                                            "      \"title\": \"답변글 제목 예시입니다\",\n" +
                                                            "      \"content\": \"문의 답변글 내용 예시입니다\",\n" +
                                                            "      \"deleted\": \"false\",\n" +
                                                            "      \"create_date\": \"2025-04-03T02:10:01.759Z\",\n" +
                                                            "      \"modified_date\": \"2025-04-03T02:10:01.759Z\"\n" +
                                                            "    },\n" +
                                                            "    {\n" +
                                                            "      \"answer_id\": \"2\",\n" +
                                                            "      \"admin_id\": \"1\",\n" +
                                                            "      \"question_id\": \"2\",\n" +
                                                            "      \"question_title\": \"문의 글 2\",\n" +
                                                            "      \"title\": \"두번째 답변글 제목입니다\",\n" +
                                                            "      \"content\": \"두번째 문의에 대한 답변글 내용입니다\",\n" +
                                                            "      \"deleted\": \"false\",\n" +
                                                            "      \"create_date\": \"2025-04-03T03:15:22.123Z\",\n" +
                                                            "      \"modified_date\": \"2025-04-03T03:15:22.123Z\"\n" +
                                                            "    }\n" +
                                                            "  ],\n" +
                                                            "  \"pageable\": {\n" +
                                                            "    \"pageNumber\": 0,\n" +
                                                            "    \"pageSize\": 10,\n" +
                                                            "    \"sort\": {\n" +
                                                            "      \"sorted\": true,\n" +
                                                            "      \"unsorted\": false,\n" +
                                                            "      \"empty\": false\n" +
                                                            "    },\n" +
                                                            "    \"offset\": 0,\n" +
                                                            "    \"paged\": true,\n" +
                                                            "    \"unpaged\": false\n" +
                                                            "  },\n" +
                                                            "  \"totalPages\": 5,\n" +
                                                            "  \"totalElements\": 42,\n" +
                                                            "  \"last\": false,\n" +
                                                            "  \"size\": 10,\n" +
                                                            "  \"number\": 0,\n" +
                                                            "  \"sort\": {\n" +
                                                            "    \"sorted\": true,\n" +
                                                            "    \"unsorted\": false,\n" +
                                                            "    \"empty\": false\n" +
                                                            "  },\n" +
                                                            "  \"numberOfElements\": 10,\n" +
                                                            "  \"first\": true,\n" +
                                                            "  \"empty\": false\n" +
                                                            "}"
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
    public ResponseEntity<Page<AnswerResponseDto>> searchPagingAnswers(@ModelAttribute AnswerSearchCondition condition,
                                                                        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(answerService.getPageByCondition(condition, pageable));
    }

    // 답변 글 리스트 조회 일반 (문의자, 카테고리, 날짜 검색조건)
    @Operation(
            summary = "답변 글 리스트 조회",
            description = "검색 조건을 쿼리스트링으로 받아 조건에 맞는 답변 글 전체 리스트를 조회합니다",
            parameters = {
                    @Parameter(name = "title", description = "제목 검색어", example = "환불"),
                    @Parameter(name = "content", description = "내용 검색어", example = "처리"),
                    @Parameter(name = "adminId", description = "관리자 ID 검색", example = "admin1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "답변 글 리스트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AnswerResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "답변 글 리스트 조회 성공 응답 예시",
                                                    summary = "조건에 맞는 답변 글 리스트",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"answer_id\": \"1\",\n" +
                                                            "    \"admin_id\": \"1\",\n" +
                                                            "    \"question_id\": \"1\",\n" +
                                                            "    \"question_title\": \"문의 글 1\",\n" +
                                                            "    \"title\": \"답변글 제목 예시입니다\",\n" +
                                                            "    \"content\": \"문의 답변글 내용 예시입니다\",\n" +
                                                            "    \"deleted\": \"false\",\n" +
                                                            "    \"create_date\": \"2025-04-03T02:10:01.759Z\",\n" +
                                                            "    \"modified_date\": \"2025-04-03T02:10:01.759Z\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"answer_id\": \"2\",\n" +
                                                            "    \"admin_id\": \"1\",\n" +
                                                            "    \"question_id\": \"2\",\n" +
                                                            "    \"question_title\": \"문의 글 2\",\n" +
                                                            "    \"title\": \"두번째 답변글 제목입니다\",\n" +
                                                            "    \"content\": \"두번째 문의에 대한 답변글 내용입니다\",\n" +
                                                            "    \"deleted\": \"false\",\n" +
                                                            "    \"create_date\": \"2025-04-03T03:15:22.123Z\",\n" +
                                                            "    \"modified_date\": \"2025-04-03T03:15:22.123Z\"\n" +
                                                            "  }\n" +
                                                            "]"
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
    @GetMapping("/list")
    public ResponseEntity<List<AnswerResponseDto>> searchListAnswers(@ModelAttribute AnswerSearchCondition condition) {

        return ResponseEntity.ok(answerService.getListByCondition(condition));
    }
}
