package com.dodo.smartsafereturn.member.entity;

import com.dodo.smartsafereturn.global.service.CloudStorageService;
import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.dto.MemberUpdateDto;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import com.dodo.smartsafereturn.member.service.MemberServiceImpl;
import com.dodo.smartsafereturn.verification.dto.SMSPasswordRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private CloudStorageService storageService;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member testMember;
    private MemberJoinDto testJoinDto;
    private MemberUpdateDto testUpdateDto;
    private MockMultipartFile testProfileImage;

    @BeforeEach
    void setUp() {
        // 테스트용 멤버 객체 설정
        testMember = Member.testBuilder()
                .memberNumber(1L)
                .id("testuser")
                .password("encodedPassword")
                .phone("01012345678")
                .profile("profile-url")
                .isDeleted(false)
                .build();

        // 테스트용 파일 설정
        testProfileImage = new MockMultipartFile(
                "file",
                "test-profile.png",
                "image/png",
                "test image content".getBytes()
        );

        // 회원가입 DTO 설정
        testJoinDto = new MemberJoinDto();
        testJoinDto.setId("testuser");
        testJoinDto.setPassword("password123");
        testJoinDto.setPhone("01012345678");
        testJoinDto.setFile(testProfileImage);

        // 회원수정 DTO 설정
        testUpdateDto = new MemberUpdateDto();
        testUpdateDto.setMemberNumber(1L);
        testUpdateDto.setPassword("newPassword");
        testUpdateDto.setPhone("01087654321");
        testUpdateDto.setFile(testProfileImage);
    }

    @Nested
    @DisplayName("회원 가입 테스트")
    class JoinTests {

        @Test
        @DisplayName("정상적인 회원 가입 처리 - 프로필 이미지 있음")
        void join_WithProfileImage_Success() {
            // given
            given(memberRepository.existsByIdAndIsDeletedIsFalse(anyString())).willReturn(false);
            given(memberRepository.existsByPhoneAndIsDeletedIsFalse(anyString())).willReturn(false);
            given(storageService.uploadFile(any(MultipartFile.class))).willReturn("new-profile-url");
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

            // when
            memberService.join(testJoinDto);

            // then
            ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
            verify(memberRepository).save(memberCaptor.capture());
            Member savedMember = memberCaptor.getValue();

            assertThat(savedMember.getId()).isEqualTo(testJoinDto.getId());
            assertThat(savedMember.getPassword()).isEqualTo("encodedPassword");
            assertThat(savedMember.getPhone()).isEqualTo(testJoinDto.getPhone());
            assertThat(savedMember.getProfile()).isEqualTo("new-profile-url");

            verify(storageService, times(1)).uploadFile(any(MultipartFile.class));
        }

        @Test
        @DisplayName("정상적인 회원 가입 처리 - 프로필 이미지 없음")
        void join_WithoutProfileImage_Success() {
            // given
            testJoinDto.setFile(null);
            given(memberRepository.existsByIdAndIsDeletedIsFalse(anyString())).willReturn(false);
            given(memberRepository.existsByPhoneAndIsDeletedIsFalse(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

            // when
            memberService.join(testJoinDto);

            // then
            ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
            verify(memberRepository).save(memberCaptor.capture());
            Member savedMember = memberCaptor.getValue();

            assertThat(savedMember.getId()).isEqualTo(testJoinDto.getId());
            assertThat(savedMember.getPassword()).isEqualTo("encodedPassword");
            assertThat(savedMember.getPhone()).isEqualTo(testJoinDto.getPhone());
            assertThat(savedMember.getProfile()).isNull();

            verify(storageService, never()).uploadFile(any(MultipartFile.class));
        }

        @Test
        @DisplayName("회원 가입 실패 - 아이디 중복")
        void join_DuplicateId_ThrowsException() {
            // given
            given(memberRepository.existsByIdAndIsDeletedIsFalse(anyString())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.join(testJoinDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("이미 존재하는 회원입니다");

            verify(memberRepository, never()).save(any(Member.class));
        }

        @Test
        @DisplayName("회원 가입 실패 - 휴대폰 중복")
        void join_DuplicatePhone_ThrowsException() {
            // given
            given(memberRepository.existsByIdAndIsDeletedIsFalse(anyString())).willReturn(false);
            given(memberRepository.existsByPhoneAndIsDeletedIsFalse(anyString())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.join(testJoinDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("이미 다른 회원이 쓰고 있는 전화번호 입니다");

            verify(memberRepository, never()).save(any(Member.class));
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class UpdateTests {

        @Test
        @DisplayName("회원 정보 수정 성공 - 모든 정보 변경")
        void update_AllFields_Success() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.of(testMember));
            given(memberRepository.existsByPhoneAndIsDeletedIsFalse(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn("newEncodedPassword");
            given(storageService.uploadFile(any(MultipartFile.class))).willReturn("updated-profile-url");

            // when
            memberService.update(testUpdateDto);

            // then
            verify(storageService).uploadFile(any(MultipartFile.class));
            verify(storageService).deleteFile(anyString());
            assertThat(testMember.getPassword()).isEqualTo("newEncodedPassword");
            assertThat(testMember.getPhone()).isEqualTo("01087654321");
            assertThat(testMember.getProfile()).isEqualTo("updated-profile-url");
        }

        @Test
        @DisplayName("회원 정보 수정 실패 - 존재하지 않는 회원")
        void update_MemberNotFound_ThrowsException() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.update(testUpdateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("존재하지 않는 회원");

            verify(storageService, never()).uploadFile(any(MultipartFile.class));
        }

        @Test
        @DisplayName("회원 정보 수정 실패 - 휴대폰 번호 중복")
        void update_DuplicatePhone_ThrowsException() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.of(testMember));
            given(memberRepository.existsByPhoneAndIsDeletedIsFalse(anyString())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> memberService.update(testUpdateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("이미 다른 회원이 쓰고 있는 전화번호 입니다");

            verify(storageService, never()).uploadFile(any(MultipartFile.class));
        }
    }

    @Nested
    @DisplayName("회원 삭제 테스트")
    class DeleteTests {

        @Test
        @DisplayName("회원 삭제 성공 - 논리적 삭제")
        void delete_Success() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.of(testMember));

            // when
            memberService.delete(1L);

            // then
            assertThat(testMember.getIsDeleted()).isTrue();
        }

        @Test
        @DisplayName("회원 삭제 실패 - 존재하지 않는 회원")
        void delete_MemberNotFound_ThrowsException() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.delete(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("존재하지 않는 회원");
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class GetMemberTests {

        @Test
        @DisplayName("단일 회원 조회 성공")
        void getMember_Success() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.of(testMember));

            // when
            MemberResponseDto result = memberService.getMember(1L);

            log.info("result {}", result);
            log.info("testMember {}", testMember);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getMemberNumber()).isEqualTo(1L);
            assertThat(result.getId()).isEqualTo(testMember.getId());
            assertThat(result.getPhone()).isEqualTo(testMember.getPhone());
            assertThat(result.getProfile()).isEqualTo(testMember.getProfile());
        }

        @Test
        @DisplayName("단일 회원 조회 실패 - 존재하지 않는 회원")
        void getMember_MemberNotFound_ThrowsException() {
            // given
            given(memberRepository.findByMemberNumberAndIsDeletedIsFalse(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.getMember(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("존재하지 않는 회원");
        }

        @Test
        @DisplayName("모든 회원 조회 성공")
        void getMembers_Success() {
            // given
            Member member1 = Member.testBuilder()
                    .memberNumber(1L)
                    .id("user1")
                    .password("password1")
                    .phone("01011112222")
                    .profile("profile1")
                    .isDeleted(false)
                    .build();

            Member member2 = Member.testBuilder()
                    .memberNumber(2L)
                    .id("user2")
                    .password("password2")
                    .phone("01033334444")
                    .profile("profile2")
                    .isDeleted(false)
                    .build();

            given(memberRepository.findAllByIsDeletedIsFalse()).willReturn(List.of(member1, member2));

            // when
            List<MemberResponseDto> results = memberService.getMembers();

            // then
            assertThat(results).hasSize(2);
            assertThat(results.get(0).getId()).isEqualTo("user1");
            assertThat(results.get(1).getId()).isEqualTo("user2");
        }
    }

    @Nested
    @DisplayName("회원 확인 테스트")
    class MemberVerificationTests {

        @Test
        @DisplayName("회원 ID와 전화번호로 회원 확인 성공")
        void isMember_ValidCredentials_ReturnsTrue() {
            // given
            SMSPasswordRequestDto dto = new SMSPasswordRequestDto();
            dto.setMemberId("testuser");
            dto.setPhone("01012345678");

            given(memberRepository.isMemberValid(anyString(), anyString())).willReturn(true);

            // when
            boolean result = memberService.isMember(dto);

            // then
            assertThat(result).isTrue();
            verify(memberRepository).isMemberValid(dto.getMemberId(), dto.getPhone());
        }

        @Test
        @DisplayName("전화번호로 회원 존재 확인 성공")
        void isExistMemberByPhone_ExistingPhone_ReturnsTrue() {
            // given
            String phone = "01012345678";
            given(memberRepository.existsByPhoneAndIsDeletedIsFalse(anyString())).willReturn(true);

            // when
            boolean result = memberService.isExistMemberByPhone(phone);

            // then
            assertThat(result).isTrue();
            verify(memberRepository).existsByPhoneAndIsDeletedIsFalse(phone);
        }

        @Test
        @DisplayName("전화번호로 회원 ID 찾기 성공")
        void findMemberIdByPhone_ExistingPhone_ReturnsId() {
            // given
            String phone = "01012345678";
            given(memberRepository.findByPhoneAndIsDeletedIsFalse(anyString()))
                    .willReturn(Optional.of(testMember));

            // when
            String result = memberService.findMemberIdByPhone(phone);

            // then
            assertThat(result).isEqualTo("testuser");
            verify(memberRepository).findByPhoneAndIsDeletedIsFalse(phone);
        }

        @Test
        @DisplayName("전화번호로 회원 ID 찾기 실패 - 존재하지 않는 회원")
        void findMemberIdByPhone_NonExistingPhone_ThrowsException() {
            // given
            String phone = "01099999999";
            given(memberRepository.findByPhoneAndIsDeletedIsFalse(anyString()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.findMemberIdByPhone(phone))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("존재하지 않는 회원");
        }

        @Test
        @DisplayName("ID로 회원 찾기 성공")
        void getMemberById_ExistingId_ReturnsMember() {
            // given
            String id = "testuser";
            given(memberRepository.findMemberByIdNotDeleted(anyString()))
                    .willReturn(Optional.of(testMember));

            // when
            Member result = memberService.getMemberById(id);

            // then
            assertThat(result).isEqualTo(testMember);
            verify(memberRepository).findMemberByIdNotDeleted(id);
        }

        @Test
        @DisplayName("ID로 회원 찾기 실패 - 존재하지 않는 회원")
        void getMemberById_NonExistingId_ThrowsException() {
            // given
            String id = "nonexistent";
            given(memberRepository.findMemberByIdNotDeleted(anyString()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberService.getMemberById(id))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("존재하지 않는 회원");
        }

        @Test
        @DisplayName("ID 중복 확인 - 중복 있음")
        void checkDuplicate_DuplicateId_ReturnsTrue() {
            // given
            String id = "testuser";
            given(memberRepository.findMemberByIdNotDeleted(anyString()))
                    .willReturn(Optional.of(testMember));

            // when
            boolean result = memberService.checkDuplicate(id);

            // then
            assertThat(result).isTrue();
            verify(memberRepository).findMemberByIdNotDeleted(id);
        }

        @Test
        @DisplayName("ID 중복 확인 - 중복 없음")
        void checkDuplicate_NoDuplicate_ReturnsFalse() {
            // given
            String id = "newuser";
            given(memberRepository.findMemberByIdNotDeleted(anyString()))
                    .willReturn(Optional.empty());

            // when
            boolean result = memberService.checkDuplicate(id);

            // then
            assertThat(result).isFalse();
            verify(memberRepository).findMemberByIdNotDeleted(id);
        }
    }
}