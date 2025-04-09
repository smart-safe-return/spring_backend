package com.dodo.smartsafereturn.verification.service;

import com.dodo.smartsafereturn.auth.dto.JwtType;
import com.dodo.smartsafereturn.auth.entity.Token;
import com.dodo.smartsafereturn.auth.entity.TokenType;
import com.dodo.smartsafereturn.auth.repository.TokenRepository;
import com.dodo.smartsafereturn.auth.utils.JwtUtil;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.verification.dto.*;
import com.dodo.smartsafereturn.member.service.MemberService;
import com.dodo.smartsafereturn.sms.service.SmsService;
import com.dodo.smartsafereturn.verification.entity.Verification;
import com.dodo.smartsafereturn.verification.entity.VerificationPurpose;
import com.dodo.smartsafereturn.verification.entity.VerificationType;
import com.dodo.smartsafereturn.verification.repository.VerificationRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository repository;
    private final MemberService memberService;
    private final SmsService smsService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
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
    public String validatePasswordBySMS(ValidateRequestDto dto) {

        // 아직 인증되지 않은 인증 정보에서 인증 객체 가져오기
        Verification verification = repository.findByIdAndVerifiedIsFalse(dto.getVerificationId())
                .orElseThrow(() -> new RuntimeException("[VerificationService] validatePasswordBySMS : 존재하지 않는 인증 정보"));

        // 성공하든 실패하든 시도 회수 +1
        verification.incrementAttempts();

        // 만료일 여부 확인
        if (verification.isExpired()) {
            log.info("[비밀번호 찾기 인증 - 검증 요청] 인증 시간이 지남");
            throw new RuntimeException("[비밀번호 찾기 인증 - 검증 요청] 인증 시간이 지남");
        }

        // 인증 코드 매칭 여부 확인
        boolean verifyCode = verification.getCode().equals(dto.getCode());

        if (!verifyCode) {
            throw new RuntimeException("[비밀번호 찾기 인증 - 검증 요청] 인증 코드 값이 일치하지 않습니다.");
        }

        // 인증 코드 값이 매칭되면 인증 여부 true 변경
        verification.changeVerified();
        // resetToken 값 생성해서 보안 토큰 반환
        return jwtUtil.generateResetToken();
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
            // 인증된 회원 아이디 반환
            return memberService.findMemberIdByPhone(dto.getPhone());
        }

        throw new RuntimeException("[아이디 찾기 인증] 인증 코드 값이 일치하지 않습니다.");
    }

    @Override
    public Long verifySignUpBySMS(SMSSignUpRequestDto dto) {

        // 인증 요청 번호가 이미 가입된 번호 인지 확인
        boolean isExist = memberService.isExistMemberByPhone(dto.getPhone());
        if (isExist) {
            throw new RuntimeException("[회원 가입 SMS 요청] 이미 등록된 휴대폰 번호입니다");
        }

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

    @Override
    public Boolean resetPassword(PasswordResetDto dto) {
        
        // resetToken 유효성 검사
        // 비밀 번호 리셋용 검증 토큰 여부 검사
        String type = jwtUtil.getType(dto.getToken());
        if (!type.equals(JwtType.RESET.getValue())) {
            throw new RuntimeException("[비밀번호 변경 요청] 비밀번호 변경 자격이 없습니다. 다시 비밀번호 찾기를 시도해주세요");
        }
        // 토큰 소멸시간 검사
        try {
            jwtUtil.isExpired(dto.getToken());
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("[비밀번호 변경 요청] 비밀번호 변경 검증 토큰이 만료되었습니다. 다시 비밀번호 찾기를 시도해주세요");
        }

        // 변경할 회원 쿼리
        Member member = memberService.getMemberById(dto.getMemberId());

        // 비밀 번호 업데이트
        memberService.update(
                MemberUpdateDto.builder()
                        .memberNumber(member.getMemberNumber())
                        .password(dto.getPassword())
                        .build()
        );

        return true;
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
