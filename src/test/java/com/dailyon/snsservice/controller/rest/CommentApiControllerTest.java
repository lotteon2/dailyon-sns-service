package com.dailyon.snsservice.controller.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        .andExpect(jsonPath("$.totalPages").isNumber())
        .andExpect(jsonPath("$.totalElements").isNumber())
        .andExpect(jsonPath("$.comments.length()").value(5))
        .andExpect(jsonPath("$.comments[0].id").isNumber())
        .andExpect(jsonPath("$.comments[0].description").isString())
        .andExpect(jsonPath("$.comments[0].createdAt").isString())
        .andExpect(jsonPath("$.comments[0].member.id").isNumber())
        .andExpect(jsonPath("$.comments[0].member.nickname").isString())
        .andExpect(jsonPath("$.comments[0].member.profileImgUrl").isString())
        .andExpect(jsonPath("$.comments[0].replyComments[0].id").isNumber())
        .andExpect(jsonPath("$.comments[0].replyComments[0].description").isString())
        .andExpect(jsonPath("$.comments[0].replyComments[0].createdAt").isString())
        .andExpect(jsonPath("$.comments[0].replyComments[0].member.id").isNumber())
        .andExpect(jsonPath("$.comments[0].replyComments[0].member.nickname").isString())
        .andExpect(jsonPath("$.comments[0].replyComments[0].member.profileImgUrl").isString());
  }

  @Test
  @DisplayName("댓글 등록")
  void createComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    String commentDescription = "댓글 123";
    CreateCommentRequest createCommentRequest =
        CreateCommentRequest.builder().description(commentDescription).build();

    String requestBody = objectMapper.writeValueAsString(createCommentRequest);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/posts/{postId}/comments", postId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("댓글 등록 - 범위를 벗어난 글자수")
  void createCommentInValidRange() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    String commentDescription = "댓글";
    CreateCommentRequest createCommentRequest =
        CreateCommentRequest.builder().description(commentDescription).build();

    String requestBody = objectMapper.writeValueAsString(createCommentRequest);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/posts/{postId}/comments", postId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(
            jsonPath("$.validation.description").value("댓글은 최소 5글자 이상 최대 140글자 이하로 입력 가능합니다."));
  }

  @Test
  @DisplayName("댓글 등록 - 댓글 미입력")
  void createCommentWithoutComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    String commentDescription = "";
    CreateCommentRequest createCommentRequest =
        CreateCommentRequest.builder().description(commentDescription).build();

    String requestBody = objectMapper.writeValueAsString(createCommentRequest);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/posts/{postId}/comments", postId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.description").value("댓글을 입력해주세요."));
  }

  @Test
  @DisplayName("답글 등록")
  void createReplyComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Long commentId = 2L;
    String replyCommentDescription = "답글 123";
    CreateReplyCommentRequest createReplyCommentRequest =
        CreateReplyCommentRequest.builder().description(replyCommentDescription).build();

    String requestBody = objectMapper.writeValueAsString(createReplyCommentRequest);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("답글 등록 - 범위를 벗어난 글자수")
  void createReplyCommentInValidRange() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Long commentId = 2L;
    String replyCommentDescription = "답글";
    CreateReplyCommentRequest createReplyCommentRequest =
        CreateReplyCommentRequest.builder().description(replyCommentDescription).build();

    String requestBody = objectMapper.writeValueAsString(createReplyCommentRequest);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(
            jsonPath("$.validation.description").value("답글은 최소 5글자 이상 최대 140글자 이하로 입력 가능합니다."));
    ;
  }

  @Test
  @DisplayName("답글 등록 - 답글 미입력")
  void createReplyCommentWithoutReplyComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Long commentId = 2L;
    String replyCommentDescription = "";
    CreateReplyCommentRequest createReplyCommentRequest =
        CreateReplyCommentRequest.builder().description(replyCommentDescription).build();

    String requestBody = objectMapper.writeValueAsString(createReplyCommentRequest);

    // when
    ResultActions resultActions =
        mockMvc.perform(
            post("/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("memberId", memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.description").value("답글을 입력해주세요."));
    ;
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteComment() throws Exception {
    // given
    Long memberId = 1L;
    Long postId = 2L;
    Long commentId = 1L;

    // when
    ResultActions resultActions =
        mockMvc.perform(
            delete("/posts/{postId}/comments/{commentId}", postId, commentId)
                .header("memberId", memberId));

    // then
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }
}
