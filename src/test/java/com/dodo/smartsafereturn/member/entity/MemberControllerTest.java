package com.dodo.smartsafereturn.member.entity;

import com.dodo.smartsafereturn.member.controller.MemberController;
import com.dodo.smartsafereturn.member.dto.MemberJoinDto;
import com.dodo.smartsafereturn.member.dto.MemberResponseDto;
import com.dodo.smartsafereturn.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@WebMvcTest(MemberController.class)
public class MemberControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberService memberService;
//
//    @Test
//    @WithMockUser(username = "testMember", roles = {"USER", "ADMIN"})
//    @DisplayName("회원 전체 리스트 조회 - 정상 케이스 [GET] /api/member")
//    void getMembers() throws Exception {
//        // given
//        Member member = Member.testBuilder()
//                .memberNumber(1L)
//                .id("test1")
//                .phone("01012345678")
//                .password("password")
//                .profile("profile1")
//                .isDeleted(false)
//                .build();
//
//        log.info("member: {}", member);
//
//        log.info("member memberNumber before return: {}", member.getMemberNumber());
//
//        MemberResponseDto responseDto = MemberResponseDto.builder()
//                .memberNumber(member.getMemberNumber())
//                .id(member.getId())
//                .phone(member.getPhone())
//                .createdDate(member.getCreatedDate())
//                .profile(member.getProfile())
//                .build();
//
//        log.info("ResponseDto memberNumber before return: {}", responseDto.getMemberNumber());
//
//        BDDMockito.given(memberService.getMembers()).willReturn(List.of(
//                responseDto
//        ));
//
//        // when & then
//        ResultActions resultActions = mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/member")
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].member_number").value(member.getMemberNumber()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(member.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone").value(member.getPhone()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created_date").value(member.getCreatedDate()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].profile").value(member.getProfile()))
//                .andDo(MockMvcResultHandlers.print());
//
//        // 검증 Assertj
//        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
//        log.info("contentAsString = {}", contentAsString);
//        assertThat(contentAsString).contains("test1");
//        assertThat(contentAsString).contains("01012345678");
//        assertThat(contentAsString).contains("profile1");
//    }


}
