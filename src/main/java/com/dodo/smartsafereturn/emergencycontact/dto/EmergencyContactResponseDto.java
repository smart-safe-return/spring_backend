package com.dodo.smartsafereturn.emergencycontact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmergencyContactResponseDto {

    private Long emergencyContactId;
    private String name;
    private String phone;
    private LocalDateTime createdDate;
}
