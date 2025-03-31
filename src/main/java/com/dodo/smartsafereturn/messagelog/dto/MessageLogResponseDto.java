package com.dodo.smartsafereturn.messagelog.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class MessageLogResponseDto {

    private Long messageLogId;
    private String message;
    private LocalDateTime createDate;

    @QueryProjection
    public MessageLogResponseDto(Long messageLogId, String message, LocalDateTime createDate) {
        this.messageLogId = messageLogId;
        this.message = message;
        this.createDate = createDate;
    }
}
