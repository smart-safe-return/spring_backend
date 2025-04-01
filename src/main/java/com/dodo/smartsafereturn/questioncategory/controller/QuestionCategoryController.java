package com.dodo.smartsafereturn.questioncategory.controller;

import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryCreateDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryResponseDto;
import com.dodo.smartsafereturn.questioncategory.dto.QuestionCategoryUpdateDto;
import com.dodo.smartsafereturn.questioncategory.service.QuestionCategoryService;
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
public class QuestionCategoryController {

    private final QuestionCategoryService service;

    // 카테고리 등록
    @PostMapping("")
    public ResponseEntity<?> create(@Validated @RequestBody QuestionCategoryCreateDto dto) {
        service.create(dto);
        return ResponseEntity.ok().build();
    }

    // 카테고리 타입 이름 변경
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody QuestionCategoryUpdateDto dto) {
        service.update(dto);
        return ResponseEntity.ok().build();
    }

    // 카테고리 삭제 (플래그 변경)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // 카테고리 리스트 조회
    @GetMapping("")
    public ResponseEntity<List<QuestionCategoryResponseDto>> getCategories() {
        return ResponseEntity.ok().body(service.getCategories());
    }
}
