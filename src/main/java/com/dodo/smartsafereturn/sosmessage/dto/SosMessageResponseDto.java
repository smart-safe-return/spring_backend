package com.dodo.smartsafereturn.sosmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SosMessageResponseDto {

    private Long sosMessageId;
    private String content;
    private LocalDateTime createdDate;
}
