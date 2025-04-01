package com.dodo.smartsafereturn.answer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerUpdateDto {

    @NotNull(message = "answer_id 는 필수값")
    private Long answerId; // 답변글 ID
    @NotBlank(message = "제목을 비우면 안됨")
    private String title;
    @NotBlank(message = "내용을 비우면 안됨")
    private String content;
}
