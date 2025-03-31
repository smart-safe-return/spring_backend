package com.dodo.smartsafereturn.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDto {

    private Long adminNumber;
    private String id;
    private LocalDateTime createDate;
}
