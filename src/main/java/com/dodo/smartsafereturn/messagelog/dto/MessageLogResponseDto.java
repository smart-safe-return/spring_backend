package com.dodo.smartsafereturn.messagelog.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class MessageLogResponseDto {

    private Long messageLogId;
    private String message;
    private LocalDateTime createDate;
    private Point location;

    @QueryProjection
    public MessageLogResponseDto(Long messageLogId, String message, LocalDateTime createDate, Point location) {
        this.messageLogId = messageLogId;
        this.message = message;
        this.createDate = createDate;
        this.location = location;
    }
}
