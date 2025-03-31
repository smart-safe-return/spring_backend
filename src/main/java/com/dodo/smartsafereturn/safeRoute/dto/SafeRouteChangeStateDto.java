package com.dodo.smartsafereturn.safeRoute.dto;

import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafeRouteChangeStateDto {

    @NotNull(message = "상태값 비어 있음")
    private RouteState state;
}
