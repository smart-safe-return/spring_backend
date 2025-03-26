package com.dodo.smartsafereturn.sosmessage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SosMessageResponseDto {

    private Long sosMessageId;
    private String content;
}
