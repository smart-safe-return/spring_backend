package com.dodo.smartsafereturn.answer.controller;

import com.dodo.smartsafereturn.answer.dto.AnswerCreateDto;
import com.dodo.smartsafereturn.answer.dto.AnswerResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerSearchCondition;
import com.dodo.smartsafereturn.answer.dto.AnswerUpdateDto;
import com.dodo.smartsafereturn.answer.service.AnswerService;
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
public class AnswerController {

    private final AnswerService answerService;

    // 답변 글 등록 (어드민)
    @PostMapping("")
    public ResponseEntity<?> createAnswer(@Validated @RequestBody AnswerCreateDto answerCreateDto) {
        Long answerId = answerService.create(answerCreateDto);
        return ResponseEntity.ok().body(answerId);
    }

    // 답변 글 수정 (어드민)
    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(@Validated @RequestBody AnswerUpdateDto answerUpdateDto) {
        answerService.update(answerUpdateDto);
        return ResponseEntity.ok().build();
    }

    // 답변 글 삭제 (어드민)
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long answerId) {
        answerService.delete(answerId);
        return ResponseEntity.ok().build();
    }

    // 답변 글 상세 정보 한건 조회 (어드민)
    @GetMapping("/{answerId}")
    public ResponseEntity<?> getAnswer(@PathVariable Long answerId) {
        return ResponseEntity.ok(answerService.getOne(answerId));
    }

    /**
     * 답변 글 리스트 조회 페이징 (문의자, 카테고리, 날짜 검색조건) -> 쿼리스트링 활용
     * - title, content, adminId (검색조건)
     * - size, sort, desc(or asc) (페이징 조건)
     * 필요한 매개값들 쿼리스트링으로 검색해서 프론트에서 요청
     */
    @GetMapping("")
    public ResponseEntity<Page<AnswerResponseDto>> searchPagingAnswers(@ModelAttribute AnswerSearchCondition condition,
                                                                        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(answerService.getPageByCondition(condition, pageable));
    }

    // 답변 글 리스트 조회 일반 (문의자, 카테고리, 날짜 검색조건)
    @GetMapping("/list")
    public ResponseEntity<List<AnswerResponseDto>> searchListAnswers(@ModelAttribute AnswerSearchCondition condition) {

        return ResponseEntity.ok(answerService.getListByCondition(condition));
    }
}
