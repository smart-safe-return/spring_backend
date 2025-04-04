package com.dodo.smartsafereturn.sms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InsufficientBalanceException extends RuntimeException {

    private final String message;
    private final long currentBalance;
    private final long currentPoint;

}
