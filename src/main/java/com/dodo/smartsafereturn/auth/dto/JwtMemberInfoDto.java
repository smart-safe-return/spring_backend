package com.dodo.smartsafereturn.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT의 Claims에 Member 정보를 넣기 위해 사용하는 dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtMemberInfoDto {

    private Long memberNumber;
    private String id;
    private String role;
    private String password;

}
