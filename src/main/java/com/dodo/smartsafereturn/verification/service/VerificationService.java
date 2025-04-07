package com.dodo.smartsafereturn.verification.service;

import com.dodo.smartsafereturn.verification.dto.*;

public interface VerificationService {

    // SMS 비밀 번호 인증 요청
    Long verifyPasswordBySMS(SMSPasswordRequestDto dto);
    // SMS 비밀 번호 인증 검증
    String validatePasswordBySMS(ValidateRequestDto dto);
    // SMS 아이디 인증 검증
    Long verifyMemberIdBySMS(SMSMemberIdRequestDto dto);
    // SMS 아이디 인증 요청
    String validateMemberIdBySMS(ValidateIdRequestDto dto);
    // SMS 회원 가입 시, 인증 요청
    Long verifySignUpBySMS(SMSSignUpRequestDto dto);
    // SMS 회원 가입 시, 인증 검증
    Boolean validateSignUpBySMS(ValidateRequestDto dto);
    // 비밀번호 변경
    Boolean resetPassword(PasswordResetDto dto);
}
