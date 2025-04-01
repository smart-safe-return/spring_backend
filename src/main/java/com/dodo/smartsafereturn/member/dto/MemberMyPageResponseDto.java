package com.dodo.smartsafereturn.member.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 정보 + 마이 페이지에서 뿌려줄 회원 관련 메시지 추가를 고려 todo
 * 회원 관련된 정보를 여러번 쿼리보단 한번에 보내는 게 나을 수 있음 (통신이 줄어듬)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberMyPageResponseDto {

    private Long memberNumber;
    private String id;
    private String phone;
    private LocalDateTime createdDate;
    private String profile;
}
