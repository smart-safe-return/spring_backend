package com.dodo.smartsafereturn.questioncategory.dto;

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
public class QuestionCategoryUpdateDto {

    @NotNull(message = "question_category_id 는 필수 값")
    private Long questionCategoryId;
    @NotBlank(message = "category 는 필수 값")
    private String category;
}
