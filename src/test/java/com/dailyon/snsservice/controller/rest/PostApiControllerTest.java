package com.dailyon.snsservice.controller.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.dailyon.snsservice.dto.request.post.CreatePostImageProductDetailRequest;
import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.entity.*;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

  @PersistenceContext private EntityManager em;

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void beforeEach() {
    Member member = Member.createMember(1L, "member1", UUID.randomUUID().toString());
    em.persist(member);

    for (int i = 1; i <= 16; i++) {
      List<PostImageProductDetail> postImageProductDetails =
          List.of(
              PostImageProductDetail.createPostImageProductDetail(
                  Integer.toUnsignedLong(i), "size", 10.0, 10.0));

      PostImage postImage =
          PostImage.createPostImage(
              String.format("/images/thumbnail%s.png", i),
              String.format("/images/image%s.png", i),
              postImageProductDetails);

      List<HashTag> hashTags = List.of(HashTag.createHashTag("태그" + i));

      Post post =
          Post.createPost(
              member,
              String.format("post %s", i),
              String.format("post %s desc", i),
              5.6,
              150.0,
              postImage,
              hashTags);
      em.persist(post);

      if (i % 2 == 0) {
        PostLike postLike = PostLike.createPostLike(member, post);
        post.addLikeCount(1);
        em.persist(postLike);
      }
    }
  }

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
  @DisplayName("게시글 등록")
  void createPost() throws Exception {
    // given
    Long memberId = 1L;
    CreatePostRequest createPostRequest =
        CreatePostRequest.builder()
            .title("post title")
            .description("post description")
            .stature(180.0)
            .weight(80.0)
            .hashTagNames(List.of("태그 1", "태그 2", "태그 3"))
            .postThumbnailImgName("thumbnail-img.png")
            .postImgName("img.png")
            .postImageProductDetails(
                List.of(
                    CreatePostImageProductDetailRequest.builder()
                        .productId(1L)
                        .productSize("XL")
                        .leftGapPercent(40.0)
                        .topGapPercent(30.0)
                        .build()))
            .build();

    String requestBody = objectMapper.writeValueAsString(createPostRequest);

    // when
    ResultActions resultActions =
        mockMvc
            .perform(
                post("/posts")
                    .header("memberId", memberId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailImgPreSignedUrl").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.imgPreSignedUrl").isString());
  }
}
