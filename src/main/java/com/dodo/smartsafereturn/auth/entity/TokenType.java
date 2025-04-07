package com.dodo.smartsafereturn.auth.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenType {
    REFRESH_TOKEN("refresh"), RESET_TOKEN("reset");

    private final String value;
}
