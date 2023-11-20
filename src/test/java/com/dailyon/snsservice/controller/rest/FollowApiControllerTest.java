package com.dailyon.snsservice.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
class FollowApiControllerTest {

  @Autowired private MockMvc mockMvc;

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
