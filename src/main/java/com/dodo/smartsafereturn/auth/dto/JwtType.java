package com.dodo.smartsafereturn.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtType {

    ACCESS("access"), REFRESH("refresh");

    private final String value;
}
