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
public class SosMessageCreateDto {

    @NotNull(message = "member_number 값을 백엔드로 보내주세요")
    private Long memberNumber;
    @NotBlank(message = "sos 메시지 내용은 필수 입력 값")
    private String content;
}
