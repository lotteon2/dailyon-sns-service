package com.dailyon.snsservice.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.dailyon.snsservice.dto.request.post.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.post.CreateReplyCommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
class CommentApiControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("댓글 등록")
  void createComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    CreateCommentRequest createCommentRequest =
        CreateCommentRequest.builder().description("댓글 123").build();

    String requestBody = objectMapper.writeValueAsString(createCommentRequest);

    // when, then
    mockMvc
        .perform(
            post("/posts/{postId}/comments", postId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("답글 등록")
  void createReplyComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Long commentId = 2L;
    CreateReplyCommentRequest createReplyCommentRequest =
        CreateReplyCommentRequest.builder().description("답글 123").build();

    String requestBody = objectMapper.writeValueAsString(createReplyCommentRequest);

    // when, then
    mockMvc
        .perform(
            post("/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 2L;
    Long commentId = 1L;

    // when, then
    mockMvc
        .perform(
            delete("/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("memberId", memberId))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
