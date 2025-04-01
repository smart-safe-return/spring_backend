package com.dodo.smartsafereturn.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSearchCondition {

    private String adminId;
    private String title;
    private String content;
}
