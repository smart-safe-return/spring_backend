package com.dodo.smartsafereturn.question.service;

import com.dodo.smartsafereturn.question.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    // 문의 글 등록 -> 등록된 question_id 값 줘서 바로 가게 해주기
    Long create(QuestionCreateDto createDto);
    // 문의 글 수정
    void update(QuestionUpdateDto updateDto);
    // 문의 글 삭제 is_delete 플래그 변경
    void delete(Long questionId);
    // 문의 글 한 건 조회
    QuestionResponseDto getOne(Long questionId);
    // 문의 글 전체 조회 todo 페이징
    List<QuestionResponseListDto> getList();
    // 문의 글 검색 조건 별 조회
    Page<QuestionResponseListDto> getListByCondition(QuestionSearchCondition condition, Pageable pageable);
}
