package com.dodo.smartsafereturn.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDto {

    @NotNull(message = "member_number 는 필수값")
    private Long memberNumber;
    @NotBlank(message = "category 는 필수값")
    private String category; // 카테고리 타입 필드
    @NotBlank(message = "title 는 필수값")
    private String title;
    @NotBlank(message = "content 는 필수값")
    private String content;
}
