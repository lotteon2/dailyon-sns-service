package com.dailyon.snsservice.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
class PostLikeApiControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("게시글 좋아요 추가")
  void createPostLike() throws Exception {
    // given
    Long memberId = 1L;
    String postIds = "4";

    // when, then
    mockMvc
        .perform(put("/posts/likes").header("memberId", memberId).param("postIds", postIds))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("게시글 좋아요 삭제")
  void deletePostLike() throws Exception {
    // given
    Long memberId = 1L;
    String postIds = "2";

    // when, then
    mockMvc
        .perform(put("/posts/likes").header("memberId", memberId).param("postIds", postIds))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("게시글 좋아요 - 벌크 추가 및 삭제")
  void togglePostLike() throws Exception {
    // given
    Long memberId = 1L;
    String postIds = "1,2,3,4";

    // when, then
    mockMvc
        .perform(put("/posts/likes").header("memberId", memberId).param("postIds", postIds))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
