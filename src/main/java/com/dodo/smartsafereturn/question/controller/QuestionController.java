package com.dodo.smartsafereturn.question.controller;

import com.dodo.smartsafereturn.auth.dto.Role;
import com.dodo.smartsafereturn.question.dto.*;
import com.dodo.smartsafereturn.question.entity.QuestionStatus;
import com.dodo.smartsafereturn.question.service.QuestionService;
import com.google.api.gax.rpc.StatusCode;
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
public class QuestionController {

    private final QuestionService questionService;

    // 문의 글 등록 -> 등록된 question_id 값 줘서 바로 가게 해주기
    @PostMapping("")
    public ResponseEntity<Long> create(@Validated @RequestBody QuestionCreateDto dto) {
        Long result = questionService.create(dto);
        return ResponseEntity.ok(result);
    }

    // 문의 글 수정
    @PutMapping("/{questionId}")
    public ResponseEntity<Void> update(@Validated @RequestBody QuestionUpdateDto dto) {
        questionService.update(dto);
        return ResponseEntity.ok().build();
    }

    // 문의 글 삭제 is_delete 플래그 변경
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> delete(@PathVariable Long questionId) {
        questionService.delete(questionId);
        return ResponseEntity.ok().build();
    }

    // 문의 글 한 건 조회
    @GetMapping("{questionId}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getOne(questionId));
    }

    // 문의 글 조회 - 회원 조건
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionByMemberNumber(@PathVariable Long memberNumber) {
        return ResponseEntity.ok(questionService.getOneByMember(memberNumber));
    }

    // 문의 글 전체 조회
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
    @GetMapping("")
    public ResponseEntity<Page<QuestionResponseListDto>> searchPagingQuestions(@ModelAttribute QuestionSearchCondition condition,
                                                @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<QuestionResponseListDto> results = questionService.getListByCondition(condition, pageable);
        return ResponseEntity.ok(results);
    }

    // 상태 변경
    @PutMapping("/{questionId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long questionId, @Validated @RequestBody QuestionUpdateStateDto dto) {
        questionService.updateStatus(questionId, dto.getStatus());
        return ResponseEntity.ok().build();
    }
}
