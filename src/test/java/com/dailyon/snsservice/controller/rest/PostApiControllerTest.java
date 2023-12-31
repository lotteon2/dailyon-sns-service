package com.dailyon.snsservice.controller.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.dailyon.snsservice.client.dto.CouponForProductResponse;
import com.dailyon.snsservice.client.dto.ProductInfoResponse;
import com.dailyon.snsservice.client.dto.ProductInfoWrapperResponse;
import com.dailyon.snsservice.client.feign.ProductServiceClient;
import com.dailyon.snsservice.client.feign.PromotionServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  @Autowired private MockMvc mockMvc;
  @MockBean private ProductServiceClient productServiceClient;
  @MockBean private PromotionServiceClient promotionServiceClient;

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
        .andExpect(jsonPath("$.hasNext").isBoolean())
        .andExpect(jsonPath("$.posts.length()").value(8))
        .andExpect(jsonPath("$.posts[0].id").isNumber())
        .andExpect(jsonPath("$.posts[0].thumbnailImgUrl").isString())
        .andExpect(jsonPath("$.posts[0].likeCount").isNumber())
        .andExpect(jsonPath("$.posts[0].viewCount").isNumber())
        .andExpect(jsonPath("$.posts[0].isLike").doesNotExist());
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
        .andExpect(jsonPath("$.hasNext").isBoolean())
        .andExpect(jsonPath("$.posts.length()").value(8))
        .andExpect(jsonPath("$.posts[0].id").isNumber())
        .andExpect(jsonPath("$.posts[0].thumbnailImgUrl").isString())
        .andExpect(jsonPath("$.posts[0].likeCount").isNumber())
        .andExpect(jsonPath("$.posts[0].viewCount").isNumber())
        .andExpect(jsonPath("$.posts[0].isLike").exists())
        .andExpect(jsonPath("$.posts[0].isLike").isBoolean());
  }

  @Test
  @DisplayName("게시글 삭제")
  void deletePost() throws Exception {
    // given
    Long postId = 1L;
    Long memberId = 1L;

    // when, then
    mockMvc
        .perform(delete("/posts/{postId}", postId).header("memberId", memberId))
        .andExpect(MockMvcResultMatchers.status().isOk());
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
        .andExpect(jsonPath("$.posts.length()").value(1))
        .andExpect(jsonPath("$.posts[0].id").isNumber())
        .andExpect(jsonPath("$.posts[0].thumbnailImgUrl").isString());
  }

  @Test
  @DisplayName("내 OOTD 사용자 게시글 조회")
  void getMyOOTDPosts() throws Exception {
    // given
    Long memberId = 2L;

    // when
    ResultActions resultActions =
        mockMvc
            .perform(get("/my-posts").header("memberId", memberId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(MockMvcResultMatchers.jsonPath("totalPages").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("totalElements").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].thumbnailImgUrl").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].likeCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].viewCount").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.posts[0].isLike").isBoolean());
  }

  @Test
  @DisplayName("Top 4 게시글 목록 조회")
  void getTop4OOTDPosts() throws Exception {
    // given
    Long productId = 101L;

    // when
    ResultActions resultActions =
        mockMvc
            .perform(get("/top4-posts").queryParam("productId", productId.toString()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(jsonPath("$.posts.length()").value(4))
        .andExpect(jsonPath("$.posts[0].id").isNumber())
        .andExpect(jsonPath("$.posts[0].thumbnailImgUrl").isString());
  }

  @Test
  @DisplayName("게시글 조회수 증가")
  void addViewCount() throws Exception {
    // given
    Long postId = 1L;

    // when,
    ResultActions resultActions =
        mockMvc.perform(
            put("/posts/{postId}/view-count", postId));

    // then
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("게시글 상세 조회 - 인증")
  void getPostDetailWithAuth() throws Exception {
    // given
    Long postId = 1L;
    Long memberId = 2L;

    Mockito.when(productServiceClient.getProductInfos(List.of(101L)))
        .thenReturn(
            ResponseEntity.ok(
                ProductInfoWrapperResponse.builder()
                    .productInfos(
                        List.of(
                            ProductInfoResponse.builder()
                                .id(101L)
                                .name("test 상품")
                                .brandName("test 브랜드")
                                .imgUrl("/test.png")
                                .price(10000)
                                .build()))
                    .build()));

    Mockito.when(promotionServiceClient.getCouponsForProduct(memberId, List.of(101L)))
        .thenReturn(
            ResponseEntity.ok(
                List.of(
                    CouponForProductResponse.builder()
                        .productId(101L)
                        .hasAvailableCoupon(true)
                        .build())));

    // when
    ResultActions resultActions =
        mockMvc
            .perform(get("/posts/{postId}", postId).header("memberId", memberId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(jsonPath("$.post.id").isNumber())
        .andExpect(jsonPath("$.post.title").isString())
        .andExpect(jsonPath("$.post.description").isString())
        .andExpect(jsonPath("$.post.stature").isNumber())
        .andExpect(jsonPath("$.post.weight").isNumber())
        .andExpect(jsonPath("$.post.imgUrl").isString())
        .andExpect(jsonPath("$.post.viewCount").isNumber())
        .andExpect(jsonPath("$.post.likeCount").isNumber())
        .andExpect(jsonPath("$.post.commentCount").isNumber())
        .andExpect(jsonPath("$.post.createdAt").isString())
        .andExpect(jsonPath("$.post.member.id").isNumber())
        .andExpect(jsonPath("$.post.member.nickname").isString())
        .andExpect(jsonPath("$.post.member.profileImgUrl").isString())
        .andExpect(jsonPath("$.post.member.code").isString())
        .andExpect(jsonPath("$.post.member.isFollowing").isBoolean())
        .andExpect(jsonPath("$.post.hashTags[0].id").isNumber())
        .andExpect(jsonPath("$.post.hashTags[0].name").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].id").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].productId").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].name").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].brandName").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].price").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].imgUrl").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].size").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].leftGapPercent").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].topGapPercent").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].hasAvailableCoupon").isBoolean());
  }

  @Test
  @DisplayName("게시글 상세 조회 - 미인증")
  void getPostDetailWithoutAuth() throws Exception {
    // given
    Long postId = 1L;
    Long memberId = null;

    Mockito.when(productServiceClient.getProductInfos(List.of(101L)))
        .thenReturn(
            ResponseEntity.ok(
                ProductInfoWrapperResponse.builder()
                    .productInfos(
                        List.of(
                            ProductInfoResponse.builder()
                                .id(101L)
                                .name("test 상품")
                                .brandName("test 브랜드")
                                .imgUrl("/test.png")
                                .price(10000)
                                .build()))
                    .build()));

    Mockito.when(promotionServiceClient.getCouponsForProduct(memberId, List.of(101L)))
        .thenReturn(ResponseEntity.ok(List.of()));

    // when
    ResultActions resultActions =
        mockMvc
            .perform(get("/posts/{postId}", postId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions
        .andExpect(jsonPath("$.post.id").isNumber())
        .andExpect(jsonPath("$.post.title").isString())
        .andExpect(jsonPath("$.post.description").isString())
        .andExpect(jsonPath("$.post.stature").isNumber())
        .andExpect(jsonPath("$.post.weight").isNumber())
        .andExpect(jsonPath("$.post.imgUrl").isString())
        .andExpect(jsonPath("$.post.viewCount").isNumber())
        .andExpect(jsonPath("$.post.likeCount").isNumber())
        .andExpect(jsonPath("$.post.commentCount").isNumber())
        .andExpect(jsonPath("$.post.createdAt").isString())
        .andExpect(jsonPath("$.post.member.id").isNumber())
        .andExpect(jsonPath("$.post.member.nickname").isString())
        .andExpect(jsonPath("$.post.member.profileImgUrl").isString())
        .andExpect(jsonPath("$.post.member.code").isString())
        .andExpect(jsonPath("$.post.member.isFollowing").doesNotExist())
        .andExpect(jsonPath("$.post.hashTags[0].id").isNumber())
        .andExpect(jsonPath("$.post.hashTags[0].name").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].id").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].productId").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].name").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].brandName").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].price").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].imgUrl").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].size").isString())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].leftGapPercent").isNumber())
        .andExpect(jsonPath("$.post.postImageProductDetails[0].topGapPercent").isNumber())
        .andExpect(
            jsonPath("$.post.member.postImageProductDetails[0].hasAvailableCoupon").doesNotExist());
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
  //            .isPostThumbnailImgExists(true)
  //            .isPostImgExists(true)
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
  //        .andExpect(jsonPath("$.thumbnailImgPreSignedUrl").isString())
  //        .andExpect(jsonPath("$.imgPreSignedUrl").isString());
  //  }
  //
  //  @Test
  //  @DisplayName("게시글 등록 - 검증 에러")
  //  void createPostWithoutProperty() throws Exception {
  //    // given
  //    Long memberId = 1L;
  //    CreatePostRequest createPostRequest =
  //        CreatePostRequest.builder()
  //            .title("제목")
  //            .description("post description")
  //            .stature(180.0)
  //            .weight(80.0)
  //            .hashTagNames(List.of())
  //            .isPostImgExists(false)
  //            .postImageProductDetails(
  //                List.of(
  //                    CreatePostImageProductDetailRequest.builder()
  //                        .productId(1L)
  //                        .productSize("XL")
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
  //            .andExpect(MockMvcResultMatchers.status().isBadRequest())
  //            .andExpect(
  //                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
  //
  //    // then
  //    resultActions
  //        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
  //        .andExpect(jsonPath("$.validation.title").value("제목은 최소 5글자 이상 최대 50글자 이하로 입력 가능합니다."))
  //        .andExpect(jsonPath("$.validation.postThumbnailImgName").value("썸네일 이미지를 등록해주세요."))
  //        .andExpect(jsonPath("$.validation.postImgName").value("이미지를 등록해주세요."))
  //        .andExpect(jsonPath("$.validation.hashTagNames").value("해시태그는 최소 1개 이상 입력해야 합니다."))
  //        .andExpect(
  //            jsonPath("$..['postImageProductDetails[0].leftGapPercent']")
  //                .value("태그된 상품의 위치를 등록해주세요."));
  //  }
}
