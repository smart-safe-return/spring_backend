package com.dodo.smartsafereturn.verification.service;

import com.dodo.smartsafereturn.verification.dto.ValidateIdRequestDto;
import com.dodo.smartsafereturn.member.service.MemberService;
import com.dodo.smartsafereturn.sms.service.SmsService;
import com.dodo.smartsafereturn.verification.dto.SMSMemberIdRequestDto;
import com.dodo.smartsafereturn.verification.dto.SMSPasswordRequestDto;
import com.dodo.smartsafereturn.verification.dto.SMSSignUpRequestDto;
import com.dodo.smartsafereturn.verification.dto.ValidateRequestDto;
import com.dodo.smartsafereturn.verification.entity.Verification;
import com.dodo.smartsafereturn.verification.entity.VerificationPurpose;
import com.dodo.smartsafereturn.verification.entity.VerificationType;
import com.dodo.smartsafereturn.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository repository;
    private final MemberService memberService;
    private final SmsService smsService;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public Long verifyPasswordBySMS(SMSPasswordRequestDto dto) {

        // 요청 등록 회원 아이디와 휴대폰 값이 유효한 지 검증
        boolean isMemberValid = memberService.isMember(dto);
        if (!isMemberValid) {
            throw new RuntimeException("등록된 회원 아이디와 전화 번호가 맞지 않습니다");
        }

        // dto 의 번호로 인증 코드 (랜덤 6자리) 보내고 인증 테이블에 인증 정보 저장
        return sendSmsAndSaveVerification(dto.getPhone(), VerificationPurpose.PASSWORD);
    }

    @Override
    public Boolean validatePasswordBySMS(ValidateRequestDto dto) {

        // 아직 인증되지 않은 인증 정보에서 인증 객체 가져오기
        Verification verification = repository.findByIdAndVerifiedIsFalse(dto.getVerificationId())
                .orElseThrow(() -> new RuntimeException("[VerificationService] validatePasswordBySMS : 존재하지 않는 인증 정보"));

        // 성공하든 실패하든 시도 회수 +1
        verification.incrementAttempts();

        // 만료일 여부 확인
        if (verification.isExpired()) {
            log.info("[비밀번호 찾기 인증 - 검증 요청] 인증 시간이 지남");
            return false;
        }

        // 인증 코드 매칭 여부 확인
        boolean verifyCode = verification.getCode().equals(dto.getCode());

        // 인증 코드 값이 매칭되면 인증 여부 true 변경
        if (verifyCode) {
            verification.changeVerified();
        }

        return verifyCode;
    }

    @Override
    public Long verifyMemberIdBySMS(SMSMemberIdRequestDto dto) {

        // 전달받은 phone 번호와 맞는 회원이 있는 지 확인
        boolean isExist = memberService.isExistMemberByPhone(dto.getPhone());

        if (!isExist) {
            throw new RuntimeException("[아이디 찾기 본인 인증 요청] 존재하지 않는 회원의 전화번호");
        }

        // dto 의 번호로 인증 코드 (랜덤 6자리) 보내고 인증 테이블에 인증 정보 저장
        return sendSmsAndSaveVerification(dto.getPhone(), VerificationPurpose.ID);
    }

    @Override
    public String validateMemberIdBySMS(ValidateIdRequestDto dto) {

        // 아직 인증되지 않은 인증 정보에서 인증 객체 가져오기
        Verification verification = repository.findByIdAndVerifiedIsFalse(dto.getVerificationId())
                .orElseThrow(() -> new RuntimeException("[VerificationService] validateMemberIdBySMS : 존재하지 않는 인증 정보"));

        // 성공하든 실패하든 시도 회수 +1
        verification.incrementAttempts();

        // 만료일 여부 확인
        if (verification.isExpired()) {
            log.info("[아이디 찾기 인증 - 검증 요청] 인증 시간이 지남");
            throw new RuntimeException("[아이디 찾기 인증 - 검증 요청] 인증 시간이 지남");
        }

        // 인증 코드 매칭 여부 확인
        boolean verifyCode = verification.getCode().equals(dto.getCode());

        // 인증 코드 값이 매칭되면 인증 여부 true 변경
        if (verifyCode) {
            verification.changeVerified();
        }

        // 인증된 회원 아이디 반환
        return memberService.findMemberIdByPhone(dto.getPhone());
    }

    @Override
    public Long verifySignUpBySMS(SMSSignUpRequestDto dto) {

        // dto 의 번호로 인증 코드 (랜덤 6자리) 보내고 인증 테이블에 인증 정보 저장
        return sendSmsAndSaveVerification(dto.getPhone(), VerificationPurpose.SIGNUP);
    }

    @Override
    public Boolean validateSignUpBySMS(ValidateRequestDto dto) {

        // 아직 인증되지 않은 인증 정보에서 인증 객체 가져오기
        Verification verification = repository.findByIdAndVerifiedIsFalse(dto.getVerificationId())
                .orElseThrow(() -> new RuntimeException("[VerificationService] validateSignUpBySMS : 존재하지 않는 인증 정보"));

        // 성공하든 실패하든 시도 회수 +1
        verification.incrementAttempts();

        // 만료일 여부 확인
        if (verification.isExpired()) {
            log.info("[회원가입 휴대폰 본인 인증 - 검증 요청] 인증 시간이 지남");
            return false;
        }

        // 인증 코드 매칭 여부 확인
        boolean verifyCode = verification.getCode().equals(dto.getCode());

        // 인증 코드 값이 매칭되면 인증 여부 true 변경
        if (verifyCode) {
            verification.changeVerified();
        }

        return verifyCode;
    }

    private Long sendSmsAndSaveVerification(String phone, VerificationPurpose purpose) {
        // 랜덤 인증 6자리 코드 생성 및 메시지 생성
        String randomCode = generate6Code();
        String content = String.format("[안전 귀가] 본인확인 인증번호 [%s]를 입력해주세요.", randomCode);
        smsService.sendSms(phone, content);

        // 인증 테이블에 정보 저장
        Verification verification = repository.save(
                Verification.builder()
                        .code(randomCode)
                        .identifier(phone)
                        .type(VerificationType.SMS)
                        .purpose(purpose)
                        .build()
        );
        return verification.getId();
    }

    private static String generate6Code() {
        return String.format("%06d", random.nextInt(1000000));
    }

}
