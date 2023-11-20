package com.dailyon.snsservice.controller.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.dailyon.snsservice.dto.request.comment.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.comment.CreateReplyCommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
class CommentApiControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("댓글 조회")
  void getComments() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Integer page = 0;
    Integer size = 5;
    String sort = "createdAt";

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                get("/posts/{postId}/comments", postId)
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments.length()").value(5))
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].description").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].createdAt").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].member.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].member.nickname").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].member.profileImgUrl").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.comments[0].replyComments[0].id").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.comments[0].replyComments[0].description").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.comments[0].replyComments[0].createdAt").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.comments[0].replyComments[0].member.id").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.comments[0].replyComments[0].member.nickname")
                .isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.comments[0].replyComments[0].member.profileImgUrl")
                .isString());
  }

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
