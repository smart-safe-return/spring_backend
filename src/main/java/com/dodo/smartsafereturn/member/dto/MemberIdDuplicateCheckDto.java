package com.dodo.smartsafereturn.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberIdDuplicateCheckDto {

    @NotBlank(message = "회원 아이디는 필수 값입니다.")
    private String id;
}
