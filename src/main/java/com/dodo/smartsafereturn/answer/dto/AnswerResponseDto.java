package com.dodo.smartsafereturn.answer.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class AnswerResponseDto {

    private Long answerId; // 답변글 ID값
    private String adminId; // 관리자 작성 문자 아이디 값
    private Long questionId; // 답변한 문의글 ID
    private String questionTitle; // 답변한 문의글 제목
    private String title;
    private String content;
    private boolean isDeleted; // 삭제 여부
    private LocalDateTime createDate; // 생성일
    private LocalDateTime modifiedDate; // 수정일

    @QueryProjection
    public AnswerResponseDto(Long answerId, String adminId, Long questionId, String questionTitle, String title, String content, boolean isDeleted, LocalDateTime createDate, LocalDateTime modifiedDate) {
        this.answerId = answerId;
        this.adminId = adminId;
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }
}
