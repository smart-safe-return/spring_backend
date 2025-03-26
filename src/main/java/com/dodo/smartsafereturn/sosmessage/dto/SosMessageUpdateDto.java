package com.dodo.smartsafereturn.sosmessage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SosMessageUpdateDto {

    @NotNull(message = "sos message id 값은 필수 입력 값")
    private Long sosMessageId;
    @NotBlank(message = "sos 메시지 내용은 필수 입력 값")
    private String content;
}
