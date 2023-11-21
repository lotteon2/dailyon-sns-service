package com.dailyon.snsservice.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.dailyon.snsservice.entity.*;
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
class PostApiControllerTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("게시글 목록 조회 - 미인증")
  void getPostsWithoutAuth() throws Exception {
    // given
    Integer page = 0;
    Integer size = 8;
    String sort = "viewCount";

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                get("/posts")
                    .queryParam("page", page.toString())
                    .queryParam("size", size.toString())
                    .queryParam("sort", sort))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("$.hasNext").isBoolean())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts.length()").value(8))
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].thumbnailImgUrl").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].likeCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].viewCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].isLike").doesNotExist());
  }

  @Test
  @DisplayName("게시글 목록 조회 - 인증")
  void getPostsWithAuth() throws Exception {
    // given
    Long memberId = 1L;
    Integer page = 0;
    Integer size = 8;
    String sort = "viewCount";

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                get("/posts")
                    .header("memberId", memberId)
                    .queryParam("page", page.toString())
                    .queryParam("size", size.toString())
                    .queryParam("sort", sort))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("$.hasNext").isBoolean())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts.length()").value(8))
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].thumbnailImgUrl").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].likeCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].viewCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].isLike").isBoolean());
  }

  @Test
  @DisplayName("게시글 좋아요 목록 조회")
  void getPostLikes() throws Exception {
    // given
    Long memberId = 1L;
    Integer page = 0;
    Integer size = 8;
    String sort = "createdAt";

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                get("/posts/likes")
                    .header("memberId", memberId)
                    .queryParam("page", page.toString())
                    .queryParam("size", size.toString())
                    .queryParam("sort", sort))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts.length()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].thumbnailImgUrl").isString());
  }

  @Test
  @DisplayName("Top 4 게시글 목록 조회")
  void getTop4OOTDPosts() throws Exception {
    // given
    Long productId = 101L;
    Long memberId = 1L;

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                get("/top4-posts")
                    .header("memberId", memberId)
                    .queryParam("productId", productId.toString()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("$.hasNext").isBoolean())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts.length()").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].thumbnailImgUrl").isString());
  }

  //  @Test
  //  @DisplayName("게시글 등록")
  //  void createPost() throws Exception {
  //    // given
  //    Long memberId = 1L;
  //    CreatePostRequest createPostRequest =
  //        CreatePostRequest.builder()
  //            .title("post title")
  //            .description("post description")
  //            .stature(180.0)
  //            .weight(80.0)
  //            .hashTagNames(List.of("태그 1", "태그 2", "태그 3"))
  //            .postThumbnailImgName("thumbnail-img.png")
  //            .postImgName("img.png")
  //            .postImageProductDetails(
  //                List.of(
  //                    CreatePostImageProductDetailRequest.builder()
  //                        .productId(1L)
  //                        .productSize("XL")
  //                        .leftGapPercent(40.0)
  //                        .topGapPercent(30.0)
  //                        .build()))
  //            .build();
  //
  //    String requestBody = objectMapper.writeValueAsString(createPostRequest);
  //
  //    // when
  //    ResultActions resultActions =
  //        mockMvc
  //            .perform(
  //                post("/posts")
  //                    .header("memberId", memberId)
  //                    .contentType(MediaType.APPLICATION_JSON_VALUE)
  //                    .content(requestBody))
  //            .andExpect(MockMvcResultMatchers.status().isCreated())
  //            .andExpect(
  //                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
  //
  //    // then
  //    resultActions
  //        .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailImgPreSignedUrl").isString())
  //        .andExpect(MockMvcResultMatchers.jsonPath("$.imgPreSignedUrl").isString());
  //  }
}
