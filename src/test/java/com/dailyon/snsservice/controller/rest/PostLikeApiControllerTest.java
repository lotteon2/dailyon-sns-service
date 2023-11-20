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
class PostLikeApiControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("게시글 좋아요 추가")
  void createPostLike() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 4L;

    // when, then
    mockMvc
        .perform(put("/posts/{postId}/likes", postId).header("memberId", memberId))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("게시글 좋아요 삭제")
  void deletePostLike() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 2L;

    // when, then
    mockMvc
        .perform(put("/posts/{postId}/likes", postId).header("memberId", memberId))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
