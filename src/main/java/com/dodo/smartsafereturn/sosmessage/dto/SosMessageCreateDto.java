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

    /**
     * todo isDeleted 플래그로 관리하니, 물리적으로 없는 지 확인해보고 생성해야함
     */
    @NotNull(message = "member_number 값을 백엔드로 보내주세요")
    private Long memberNumber;
    @NotBlank(message = "sos 메시지 내용은 필수 입력 값")
    private String content;
}
