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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
class FollowApiControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("팔로잉 목록 조회")
  void getFollowings() throws Exception {
    // given
    Long memberId = 1L;
    Integer page = 0;
    Integer size = 8;
    String sort = "createdAt";

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                get("/follows/followings")
                    .header("memberId", memberId)
                    .queryParam("page", page.toString())
                    .queryParam("size", size.toString())
                    .queryParam("sort", sort))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.followings.length()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.followings[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.followings[0].nickname").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.followings[0].profileImgUrl").isString());
  }

  @Test
  @DisplayName("팔로우 추가")
  void createFollow() throws Exception {
    // given
    Long memberId = 1L;
    Long followingId = 4L;

    // when, then
    mockMvc
        .perform(put("/follows/{followingId}", followingId).header("memberId", memberId))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("팔로우 삭제")
  void deleteFollow() throws Exception {
    // given
    Long memberId = 1L;
    Long followingId = 2L;

    // when, then
    mockMvc
        .perform(put("/follows/{followingId}", followingId).header("memberId", memberId))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
