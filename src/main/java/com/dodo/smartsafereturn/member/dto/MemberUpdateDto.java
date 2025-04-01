package com.dodo.smartsafereturn.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberUpdateDto {

    @NotNull(message = "member_number 는 필수값")
    private Long memberNumber;
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다."
    )
    private String password;
    @Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다."
    )
    private String phone;
    private MultipartFile file;
    private String profile; // 실제 파일을 스토리지 저장 후, 주소 DB 저장용
}
