package com.dodo.smartsafereturn.question.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 문의글 상세 페이지용
 */
@Data
@Builder
@NoArgsConstructor
public class QuestionResponseDto {

    private Long questionId;
    private String memberId;
    private String category;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @QueryProjection
    public QuestionResponseDto(Long questionId, String memberId, String category, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.questionId = questionId;
        this.memberId = memberId;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
