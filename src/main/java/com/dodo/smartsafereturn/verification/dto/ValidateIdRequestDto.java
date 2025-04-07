package com.dodo.smartsafereturn.verification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateIdRequestDto {
    /**
     *  인증 코드 검증 요청 dto
     */

    // 아이디 찾기일 시, 휴대폰 번호를 기반으로 아이다를 찾아야 함.
    @Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다."
    )
    @NotBlank
    private String phone;
    @NotNull(message = "verification_id 아이디 값은 필수값")
    private Long verificationId;
    @NotBlank(message = "code 인증 코드 값은 필수값")
    private String code;
}
