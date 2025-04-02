package com.dodo.smartsafereturn.messagelog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageLogCreateDto {

    @NotNull(message = "귀가 루트 ID 필요")
    private Long safeRouteId;
    @NotBlank(message = "메시지는 비어있으면 안됨")
    private String message;
    // sms 발송 위치 데이터
    private Point location;
}
