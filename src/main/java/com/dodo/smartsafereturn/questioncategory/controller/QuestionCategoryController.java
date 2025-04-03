package com.dodo.smartsafereturn.questioncategory.controller;

import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryCreateDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryResponseDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryUpdateDto;
import com.dodo.smartsafereturn.questioncategory.service.QuestionCategoryService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/question-category")
@RequiredArgsConstructor
@Tag(name = "문의 카테고리 관리", description = "문의 카테고리 CRUD API")
public class QuestionCategoryController {

    private final QuestionCategoryService service;

    // 카테고리 등록
    @Operation(
            summary = "문의 카테고리 등록",
            description = "새로운 문의 카테고리를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 카테고리 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionCategoryCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "카테고리 등록 요청 예시",
                                            summary = "문의 카테고리 등록에 필요한 데이터",
                                            value = "{\n" +
                                                    "  \"category\": \"FEEDBACK\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카테고리 등록 성공"
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
    public ResponseEntity<?> create(@Validated @RequestBody QuestionCategoryCreateDto dto) {
        service.create(dto);
        return ResponseEntity.ok().build();
    }

    // 카테고리 타입 이름 변경
    @Operation(
            summary = "문의 카테고리 수정",
            description = "기존 문의 카테고리의 정보를 수정합니다.",
            parameters = {
                    @Parameter(name = "id", description = "수정할 카테고리 ID", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 카테고리 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = QuestionCategoryUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "카테고리 수정 요청 예시",
                                            summary = "문의 카테고리 수정에 필요한 데이터",
                                            value = "{\n" +
                                                    "  \"question_category_id\": 1,\n" +
                                                    "  \"category\": \"피드백_수정\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카테고리 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "카테고리를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody QuestionCategoryUpdateDto dto, @PathVariable String id) {
        service.update(dto);
        return ResponseEntity.ok().build();
    }

    // 카테고리 삭제 (플래그 변경)
    @Operation(
            summary = "문의 카테고리 삭제",
            description = "문의 카테고리를 삭제합니다. (삭제 플래그 변경)",
            parameters = {
                    @Parameter(name = "id", description = "삭제할 카테고리 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카테고리 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "카테고리를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // 카테고리 리스트 조회
    @Operation(
            summary = "문의 카테고리 목록 조회",
            description = "모든 문의 카테고리 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카테고리 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuestionCategoryResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "카테고리 목록 응답 예시",
                                                    summary = "문의 카테고리 목록",
                                                    value = "[\n" +
                                                            "  {\n" +
                                                            "    \"question_category_id\": 1,\n" +
                                                            "    \"category\": \"배송 문의\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"question_category_id\": 2,\n" +
                                                            "    \"category\": \"버그제보\"\n" +
                                                            "  },\n" +
                                                            "  {\n" +
                                                            "    \"question_category_id\": 3,\n" +
                                                            "    \"category\": \"피드백\"\n" +
                                                            "  }\n" +
                                                            "]"
                                            )
                                    }
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public ResponseEntity<List<QuestionCategoryResponseDto>> getCategories() {
        return ResponseEntity.ok().body(service.getCategories());
    }
}