package com.dodo.smartsafereturn.questioncategory.service;

import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryCreateDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryResponseDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryUpdateDto;

import java.util.List;

public interface QuestionCategoryService {

    // 카테고리 등록
    void create(QuestionCategoryCreateDto dto);
    // 카테고리 타입 이름 변경
    void update(QuestionCategoryUpdateDto dto);
    // 카테고리 삭제 (플래그 변경)
    void delete(Long questionCategoryId);
    // 카테고리 리스트 조회
    List<QuestionCategoryResponseDto> getCategories();

}
