package com.dodo.smartsafereturn.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberResponseDto {

    private Long memberNumber;
    private String id;
    private String phone;
    private LocalDateTime createdDate;
    private String profile;
}
