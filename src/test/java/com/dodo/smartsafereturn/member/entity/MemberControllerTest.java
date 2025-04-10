package com.dodo.smartsafereturn.member.entity;

import com.dodo.smartsafereturn.member.controller.MemberController;
import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@Slf4j
@WebMvcTest(MemberController.class)
@Description("컨트롤러가 서비스 메서드를 올바른 인자로 호출하는지 검증")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @WithMockUser(username = "testMember", roles = {"USER", "ADMIN"})
    @DisplayName("회원 전체 리스트 조회 - 정상 케이스 [GET] /api/member")
    void getMembers() throws Exception {
        // given
        Member member = Member.testBuilder()
                .memberNumber(1L)
                .id("test1")
                .phone("01012345678")
                .password("password")
                .profile("profile1")
                .isDeleted(false)
                .build();

        log.info("member: {}", member);

        log.info("member memberNumber before return: {}", member.getMemberNumber());

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .memberNumber(member.getMemberNumber())
                .id(member.getId())
                .phone(member.getPhone())
                .createdDate(member.getCreatedDate())
                .profile(member.getProfile())
                .build();

        log.info("ResponseDto memberNumber before return: {}", responseDto.getMemberNumber());

        BDDMockito.given(memberService.getMembers()).willReturn(List.of(
                responseDto
        ));

        // when & then
        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/member")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].member_number").value(member.getMemberNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(member.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone").value(member.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created_date").value(member.getCreatedDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].profile").value(member.getProfile()))
                .andDo(MockMvcResultHandlers.print());

        // 검증 Assertj
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        log.info("contentAsString = {}", contentAsString);
        assertThat(contentAsString).contains("test1");
        assertThat(contentAsString).contains("01012345678");
        assertThat(contentAsString).contains("profile1");
    }

    @Test
    @WithMockUser(username = "testMember")
    @DisplayName("회원 가입 - 정상 케이스 [POST] /api/member")
    void memberJoin() throws Exception {

        // given
        MockMultipartFile profileImage = new MockMultipartFile(
                "file",
                "test-profile.png",
                "image/png",
                "test image content".getBytes()
        );

        // ArgumentCaptor 설정 : MemberJoinDto 타입 인자 캡쳐 준비
        ArgumentCaptor<MemberJoinDto> memberJoinDtoCaptor = ArgumentCaptor.forClass(MemberJoinDto.class);


        // when then
        mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/member")
                        .file(profileImage)
                        .param("id", "test")
                        .param("phone", "01012345678")
                        .param("password", "!password1234")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()) // csrf 토큰 (기본 인증 형태는 필요)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 서비스 파라미터 호출 유효성 검증
        verify(memberService).join(memberJoinDtoCaptor.capture());
        MemberJoinDto dto = memberJoinDtoCaptor.getValue();

        // controller 에 받아온 RequestBody 파라미터들 검증
        assertThat(dto.getId()).isEqualTo("test");
        assertThat(dto.getPhone()).isEqualTo("01012345678");
        assertThat(dto.getPassword()).isEqualTo("!password1234");
        assertThat(dto.getFile().getOriginalFilename()).isEqualTo("test-profile.png");


    }


    @Test
    @WithMockUser(username = "testMember")
    @DisplayName("회원 한 건 조회 마이페이지용 - 정상 케이스 [GET] /api/member/{memberId}")
    void getMemberOne() throws Exception {
        // given
        Member member = Member.testBuilder()
                .memberNumber(1L)
                .id("test2")
                .phone("01012345671")
                .password("password")
                .profile("profile2")
                .isDeleted(false)
                .build();


        MemberResponseDto responseDto = MemberResponseDto.builder()
                .memberNumber(member.getMemberNumber())
                .id(member.getId())
                .phone(member.getPhone())
                .createdDate(member.getCreatedDate())
                .profile(member.getProfile())
                .build();

        BDDMockito.given(memberService.getMember(1L)).willReturn(responseDto);

        // when & then
        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/member/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.member_number").value(member.getMemberNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(member.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(member.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_date").value(member.getCreatedDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profile").value(member.getProfile()))
                .andDo(MockMvcResultHandlers.print());

        // 검증 Assertj
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(contentAsString).contains("test2");
        assertThat(contentAsString).contains("01012345671");
        assertThat(contentAsString).contains("profile2");
    }

}
