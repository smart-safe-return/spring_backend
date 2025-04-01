package com.dodo.smartsafereturn.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberJoinDto {
    /**
     * 회원 가입 요청 Dto
     * - 아이디
     * - 비밀번호
     * - 휴대폰 번호
     */
    @NotBlank(message = "아이디는 필수 입력 값")
    private String id;
    @NotBlank(message = "비밀번호는 필수 입력 값")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다."
    )
    private String password;
    @NotBlank(message = "전화 번호는 필수 입력 값")
    @Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다."
    )
    private String phone;
    // profile 사진 업로드 파일 todo 검증 어노테이션 뭐쓸지 생각
    private MultipartFile file;
}
