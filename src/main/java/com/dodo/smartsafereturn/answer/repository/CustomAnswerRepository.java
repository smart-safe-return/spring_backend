package com.dodo.smartsafereturn.answer.repository;

import com.dodo.smartsafereturn.answer.dto.AnswerResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomAnswerRepository {
    // 답변 글 리스트 조회 페이징 (문의자, 카테고리, 날짜 검색조건)
    Page<AnswerResponseDto> getPageByCondition(AnswerSearchCondition condition, Pageable pageable);
    // 답변 글 리스트 조회 일반 (문의자, 카테고리, 날짜 검색조건)
    List<AnswerResponseDto> getListByCondition(AnswerSearchCondition condition);
}
