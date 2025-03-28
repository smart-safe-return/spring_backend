package com.dodo.smartsafereturn.emergencycontact.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmergencyContactUpdateDto {

    @NotNull
    private Long emergencyContactId;
    @NotBlank(message = "이름은 필수 입력 값")
    private String name;
    @NotBlank(message = "전화 번호는 필수 입력 값")
    @Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다."
    )
    private String phone;
}
