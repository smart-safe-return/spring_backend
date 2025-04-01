package com.dodo.smartsafereturn.answer.service;

import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerCreateDto;
import com.dodo.smartsafereturn.answer.dto.AnswerResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerSearchCondition;
import com.dodo.smartsafereturn.answer.dto.AnswerUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerService {

    // 답변 글 등록 (어드민)
    Long create(AnswerCreateDto createDto);
    // 답변 글 수정 (어드민)
    void update(AnswerUpdateDto updateDto);
    // 답변 글 삭제 (어드민) - 플래그만 변경
    void delete(Long answerId);
    // 답변 글 상세 정보 한건 조회 (어드민)
    AnswerResponseDto getOne(Long answerId);
    // 답변 글 리스트 조회 페이징 (문의자, 카테고리, 날짜 검색조건)
    Page<AnswerResponseDto> getPageByCondition(AnswerSearchCondition condition, Pageable pageable);
    // 답변 글 리스트 조회 일반 (문의자, 카테고리, 날짜 검색조건)
    List<AnswerResponseDto> getListByCondition(AnswerSearchCondition condition);
}
