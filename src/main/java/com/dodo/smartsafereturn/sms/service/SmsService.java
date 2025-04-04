package com.dodo.smartsafereturn.sms.service;

import java.util.List;

public interface SmsService {

    // 기본 sos 문자 보내기
    void sendSms(String to, String content);
    // 여러명에게 보내기
    void sendSmsToMany(List<String> toList, String content);
    // 잔액 조회
    long getBalance();
    // 포인 잔액 조회
    long getPoint();
}
