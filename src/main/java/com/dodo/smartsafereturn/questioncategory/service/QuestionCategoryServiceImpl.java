package com.dodo.smartsafereturn.questioncategory.service;

import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryCreateDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryResponseDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryUpdateDto;
import com.dodo.smartsafereturn.questioncategory.entity.QuestionCategory;
import com.dodo.smartsafereturn.questioncategory.repository.QuestionCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCategoryServiceImpl implements QuestionCategoryService {

    private final QuestionCategoryRepository repository;

    @Transactional
    @Override
    public void create(QuestionCategoryCreateDto dto) {
        // category 타입 값 안겹치게 하기
        boolean isExist = repository.existsByCategoryAndIsDeletedIsFalse(dto.getCategory());
        if (isExist) {
            throw new RuntimeException("이미 존재하는 카테고리 타입입니다.");
        }

        repository.save(
                QuestionCategory.builder()
                        .category(dto.getCategory())
                        .build()
        );
    }

    @Transactional
    @Override
    public void update(QuestionCategoryUpdateDto dto) {
        QuestionCategory category = repository.findByIdAndIsDeletedIsFalse(dto.getQuestionCategoryId())
                .orElseThrow(() -> new RuntimeException("[QuestionCategoryService] 존재하지 않는 카테고리"));

        category.update(dto.getCategory());

    }

    @Transactional
    @Override
    public void delete(Long questionCategoryId) {
        QuestionCategory category = repository.findByIdAndIsDeletedIsFalse(questionCategoryId)
                .orElseThrow(() -> new RuntimeException("[QuestionCategoryService] 존재하지 않는 카테고리"));

        if (!category.getIsDeleted()) {
            category.changeIsDeleted();
        }
    }

    @Override
    public List<QuestionCategoryResponseDto> getCategories() {

        return repository.findAllByIsDeletedIsFalse()
                .stream()
                .map(
                        c -> QuestionCategoryResponseDto.builder()
                                .questionCategoryId(c.getId())
                                .category(c.getCategory())
                                .build()
                )
                .toList();
    }
}
