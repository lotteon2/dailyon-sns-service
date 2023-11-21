package com.dailyon.snsservice.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
class MemberApiControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("OOTD 사용자 프로필 조회")
  void getOOTDMemberProfile() throws Exception {
    // given
    Long memberId = 2L;
    Long followerId = 1L;

    // when
    ResultActions resultActions =
        mockMvc
            .perform(get("/members/{memberId}", memberId).header("memberId", followerId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("member.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("member.nickname").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("member.profileImgUrl").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("member.followingCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("member.followerCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("member.isFollowing").isBoolean());
  }

  @Test
  @DisplayName("OOTD 사용자 게시글 조회")
  void getOOTDPosts() throws Exception {
    // given
    Long memberId = 2L;
    Long targetId = 1L;

    // when
    ResultActions resultActions =
        mockMvc
            .perform(get("/members/{memberId}/posts", targetId).header("memberId", memberId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("hasNext").isBoolean())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].thumbnailImgUrl").isString());
  }
}
