package com.dodo.smartsafereturn.member.service;

import com.dodo.smartsafereturn.global.service.CloudStorageService;
import com.dodo.smartsafereturn.member.dto.*;
import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import com.dodo.smartsafereturn.verification.dto.SMSPasswordRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CloudStorageService storageService;

    @Transactional
    @Override
    public void join(MemberJoinDto memberJoinDto) {

        // 이미 회원 가입된 회원인지 확인 (ID값)
        boolean isExist = memberRepository.existsByIdAndIsDeletedIsFalse(memberJoinDto.getId());
        if (isExist) {
            log.info("[MemberService] Join() 예외 발생 : [회원 가입 로직] 이미 존재하는 회원입니다");
            throw  new RuntimeException("[회원 가입 로직] 이미 존재하는 회원입니다");
        }

        // 휴대폰 번호 중복 검사
        boolean duplicatePhone = memberRepository.existsByPhoneAndIsDeletedIsFalse(memberJoinDto.getPhone());
        if (duplicatePhone) {
            throw new RuntimeException("[회원 가입 로직] : 이미 다른 회원이 쓰고 있는 전화번호 입니다");
        }


        // 프로필 이미지 저장
        String profileUrl = null;
        MultipartFile profileImage = memberJoinDto.getFile();

        if (profileImage != null && !profileImage.isEmpty()) {
            profileUrl = storageService.uploadFile(profileImage);
            log.info("[MemberService] 프로필 이미지 업로드 완료: {}", profileUrl);
        }
        
        // 회원 가입
        memberRepository.save(
                Member.builder()
                        .id(memberJoinDto.getId())
                        .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                        .phone(memberJoinDto.getPhone())
                        .profile(profileUrl) // 기본값 null
                        .build()
        );
    }

    @Transactional
    @Override
    public void update(MemberUpdateDto memberUpdateDto) {
        // 회원 존재 여부 검증
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(memberUpdateDto.getMemberNumber())
                .orElseThrow(() -> new RuntimeException("[MemberService] update() : 존재하지 않는 회원"));

        // 휴대폰 번호 중복 검사
        if (memberUpdateDto.getPhone() != null && !memberUpdateDto.getPhone().isEmpty()) {
            boolean duplicatePhone = memberRepository.existsByPhoneAndIsDeletedIsFalse(memberUpdateDto.getPhone());
            if (duplicatePhone) {
                throw new RuntimeException("[회원 정보 수정] : 이미 다른 회원이 쓰고 있는 전화번호 입니다");
            }
        }

        // 비밀번호 암호화
        if (memberUpdateDto.getPassword() != null && !memberUpdateDto.getPassword().isEmpty()) {
            memberUpdateDto.setPassword(passwordEncoder.encode(memberUpdateDto.getPassword()));
        }

        // 기존 프로필 이미지 정리
        String oldProfile = null;
        if (member.getProfile() != null && !member.getProfile().isEmpty()) {
            oldProfile = member.getProfile();
        }

        // 변경할 이미지 있을 시, 변경
        MultipartFile profileImage = memberUpdateDto.getFile();
        if (profileImage != null && !profileImage.isEmpty()) {
            String updatedProfileUrl = storageService.uploadFile(profileImage);
            memberUpdateDto.setProfile(updatedProfileUrl);
            log.info("[MemberService] 프로필 이미지 업데이트 완료: {}", updatedProfileUrl);

            // 변경 후, 기존 이미지 파일 삭제
            if (StringUtils.hasText(oldProfile)) {
                try {
                    storageService.deleteFile(oldProfile);
                } catch (Exception e) {
                    // 기존 이미지 삭제 실패 시 로그만 남기고 진행 (트랜잭션 롤백 방지)
                    log.error("기존 프로필 이미지 삭제 실패: {}, 사유: {}", oldProfile, e.getMessage());
                }
            }
        }

        // dirty checking 활용
        member.updateMember(memberUpdateDto);
    }

    @Transactional
    @Override
    public void delete(Long memberNumber) {

        // 존재 여부 체크
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(memberNumber)
                .orElseThrow(() -> new RuntimeException("[MemberService] delete() : 존재하지 않는 회원"));

        // is_Deleted 플래그 변경 -> dirty checking
        if (!member.getIsDeleted()) {
            member.changeDeleteFlag();
        }
    }

    @Override
    public MemberResponseDto getMember(Long memberNumber) {

        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(memberNumber)
                .orElseThrow(() -> new RuntimeException("[MemberService] getMember() : 존재하지 않는 회원"));

        return MemberResponseDto.builder()
                .memberNumber(memberNumber)
                .id(member.getId())
                .phone(member.getPhone())
                .createdDate(member.getCreatedDate())
                .profile(member.getProfile())
                .build();
    }

    @Override
    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAllByIsDeletedIsFalse()
                .stream()
                .map(m -> MemberResponseDto.builder()
                        .memberNumber(m.getMemberNumber())
                        .id(m.getId())
                        .phone(m.getPhone())
                        .createdDate(m.getCreatedDate())
                        .profile(m.getProfile())
                        .build())
                .toList();
    }

    @Override
    public boolean isMember(SMSPasswordRequestDto dto) {
        return memberRepository.isMemberValid(dto.getMemberId(), dto.getPhone());
    }

    @Override
    public boolean isExistMemberByPhone(String phone) {
        return memberRepository.existsByPhoneAndIsDeletedIsFalse(phone);
    }

    @Override
    public String findMemberIdByPhone(String phone) {
        Member member = memberRepository.findByPhoneAndIsDeletedIsFalse(phone)
                .orElseThrow(() -> new RuntimeException("[MemberService] findMemberIdByPhone() : 존재하지 않는 회원"));
        return member.getId();
    }

    @Override
    public Member getMemberById(String id) {
        return memberRepository.findMemberByIdNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("[MemberService] getMemberById() : 존재하지 않는 회원"));
    }

    @Override
    public boolean checkDuplicate(String id) {
        return memberRepository.findMemberByIdNotDeleted(id).isPresent();
    }

    @Override
    public boolean checkPassword(PasswordCheckDto dto) {

        Member member = memberRepository.findMemberByIdNotDeleted(dto.getId())
                .orElseThrow(() -> new RuntimeException("[MemberService] checkPassword() : 존재하지 않는 회원"));

        return passwordEncoder.matches(dto.getPassword(), member.getPassword());
    }
}
