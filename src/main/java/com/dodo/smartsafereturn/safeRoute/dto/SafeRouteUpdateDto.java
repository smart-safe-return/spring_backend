package com.dodo.smartsafereturn.safeRoute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SafeRouteUpdateDto {

    @NotNull
    private Long safeRouteId;
    @NotBlank(message = "도착지를 적어주세요")
    private String endLocation;
    @NotNull(message = "도착시간을 적어주세요")
    private LocalDateTime endTime;
}
