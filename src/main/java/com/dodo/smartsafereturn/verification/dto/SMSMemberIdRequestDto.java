package com.dodo.smartsafereturn.verification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMSMemberIdRequestDto {
    /**
     * 아이디 찾기 요청 dto
     * - 본인 전화 번호 기반으로 해야함
     */
    @NotBlank(message = "phone 값은 필수값")
    @Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다."
    )
    private String phone;
}
