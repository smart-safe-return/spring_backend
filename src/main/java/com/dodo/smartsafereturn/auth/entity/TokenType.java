package com.dodo.smartsafereturn.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    REFRESH_TOKEN("refresh"), RESET_TOKEN("reset");

    private final String value;
}
