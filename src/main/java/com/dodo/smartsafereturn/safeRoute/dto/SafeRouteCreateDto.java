package com.dodo.smartsafereturn.safeRoute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafeRouteCreateDto {

    @NotNull(message = "회원 PK 값을 넣어주세요")
    private Long memberNumber;
    @NotBlank(message = "출발지를 적어주세요")
    private String startLocation;
    @NotBlank(message = "도착지를 적어주세요")
    private String endLocation;
    @NotNull(message = "출발시간을 적어주세요")
    private LocalDateTime startTime;
    @NotNull(message = "도착시간을 적어주세요")
    private LocalDateTime endTime;
    // 경로 데이터
    private LineString routePath;
}
