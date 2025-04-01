package com.dodo.smartsafereturn.question.dto;

import com.dodo.smartsafereturn.question.entity.QuestionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateStateDto {

    @NotNull(message = "status 는 필수값")
    private QuestionStatus status;
}
