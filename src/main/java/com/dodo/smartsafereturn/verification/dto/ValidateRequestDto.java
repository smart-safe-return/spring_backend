package com.dodo.smartsafereturn.verification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRequestDto {
    /**
     *  인증 코드 검증 요청 dto
     */
    @NotNull(message = "verification_id 아이디 값은 필수값")
    private Long verificationId;
    @NotBlank(message = "code 인증 코드 값은 필수값")
    private String code;
}
