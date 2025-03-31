package com.dodo.smartsafereturn.question.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 문의글 검색 조건
 * - 제목 포함
 * - 내용 포함
 * - 카테고리 타입
 * - 작성자 ID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchCondition {

    private String title;
    private String content;
    private String memberId;
    private String category;
}
