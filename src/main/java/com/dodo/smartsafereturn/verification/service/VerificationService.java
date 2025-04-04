package com.dodo.smartsafereturn.verification.service;

import com.dodo.smartsafereturn.verification.dto.SMSMemberIdRequestDto;
import com.dodo.smartsafereturn.verification.dto.SMSPasswordRequestDto;
import com.dodo.smartsafereturn.verification.dto.SMSSignUpRequestDto;
import com.dodo.smartsafereturn.verification.dto.ValidateRequestDto;

public interface VerificationService {

    // SMS 비밀 번호 인증 요청
    Long verifyPasswordBySMS(SMSPasswordRequestDto dto);
    // SMS 비밀 번호 인증 검증
    Boolean validatePasswordBySMS(ValidateRequestDto dto);
    // SMS 아이디 인증 검증
    Long verifyMemberIdBySMS(SMSMemberIdRequestDto dto);
    // SMS 아이디 인증 요청
    Boolean validateMemberIdBySMS(ValidateRequestDto dto);
    // SMS 회원 가입 시, 인증 요청
    Long verifySignUpBySMS(SMSSignUpRequestDto dto);
    // SMS 회원 가입 시, 인증 검증
    Boolean validateSignUpBySMS(ValidateRequestDto dto);

}
