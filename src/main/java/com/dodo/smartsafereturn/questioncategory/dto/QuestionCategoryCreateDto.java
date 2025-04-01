package com.dodo.smartsafereturn.questioncategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCategoryCreateDto {

    @NotBlank(message = "category 는 필수 값")
    private String category;
}
