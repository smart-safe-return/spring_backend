package com.dodo.smartsafereturn.question.repository;

import com.dodo.smartsafereturn.question.dto.QuestionResponseDto;
import com.dodo.smartsafereturn.question.dto.QuestionResponseListDto;
import com.dodo.smartsafereturn.question.dto.QuestionSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQuestionRepository {
    Page<QuestionResponseListDto> searchQuestionByCondition(QuestionSearchCondition condition, Pageable pageable);
    QuestionResponseDto getQuestionById(Long questionId);
}
