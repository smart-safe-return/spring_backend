package com.dodo.smartsafereturn.safeRoute.dto;


import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafeRouteResponseDto {

    private Long safeRouteId;
    private Long memberNumber;
    private String startLocation;
    private String endLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RouteState isSuccess;
}
