package com.dailyon.snsservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.post.OOTDPostPageResponse;
import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.dto.response.post.Top4OOTDResponse;
import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.service.post.PostService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostServiceTest {

  static {
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
  }

  @Autowired private PostService postService;
  @PersistenceContext private EntityManager em;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("게시글 목록 조회 - 미인증")
  void getPostsWithoutAuth() {
    // given
    Long memberId = null;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    PostPageResponse postPageResponse = postService.getPosts(memberId, pageRequest);

    // then
    assertThat(postPageResponse.getHasNext()).isTrue();
    assertThat(postPageResponse.getPosts().size()).isSameAs(8);
    postPageResponse.getPosts().forEach(p -> assertThat(p.getIsLike()).isNull());
  }

  @Test
  @DisplayName("게시글 목록 조회 - 인증")
  void getPostsWithAuth() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    PostPageResponse postPageResponse = postService.getPosts(memberId, pageRequest);

    // then
    assertThat(postPageResponse.getHasNext()).isTrue();
    assertThat(postPageResponse.getPosts().size()).isSameAs(8);
    postPageResponse.getPosts().forEach(p -> assertThat(p.getIsLike()).isNotNull());
  }

  @Test
  @DisplayName("게시글 좋아요 목록 조회")
  void getPostLikes() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    PostLikePageResponse postLikePageResponse = postService.getPostLikes(memberId, pageRequest);

    // then
    assertThat(postLikePageResponse.getHasNext()).isFalse();
    assertThat(postLikePageResponse.getPosts().size()).isSameAs(1);
  }

  @Test
  @DisplayName("OOTD 게시글 목록 조회")
  void getOOTDPosts() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    OOTDPostPageResponse ootdPostPageResponse = postService.getOOTDPosts(memberId, pageRequest);

    // then
    assertThat(ootdPostPageResponse.getHasNext()).isFalse();
    assertThat(ootdPostPageResponse.getPosts().size()).isSameAs(6);
    ootdPostPageResponse
        .getPosts()
        .forEach(
            post -> {
              assertThat(post.getId()).isNotNull();
              assertThat(post.getThumbnailImgUrl()).isNotNull();
            });
  }

  @Test
  @DisplayName("OOTD Top4 게시글 목록 조회")
  void getTop4OOTDPosts() {
    // given
    Long productId = 101L;

    // when
    List<Top4OOTDResponse> top4OOTDResponses = postService.getTop4OOTDPosts(productId);

    // then
    assertThat(top4OOTDResponses.size()).isSameAs(4);
  }

  @Test
  @DisplayName("게시글 삭제")
  void softDeletePost() {
    // given
    Long postId = 1L;
    Long memberId = 1L;

    // when
    postService.softDeletePost(postId, memberId);

    // then
    String postCountVOStringValue =
        redisTemplate.opsForValue().get(String.format("postCount::%s", postId));

    assertThat(postCountVOStringValue).isNull();
    assertThrowsExactly(
        NoResultException.class,
        () ->
            em.createQuery(
                    "select p from Post p where p.id = :postId and p.isDeleted = false", Post.class)
                .setParameter("postId", postId)
                .getSingleResult());
  }

  @Test
  @DisplayName("게시글 조회수 증가")
  void addViewCount() throws JsonProcessingException {
    // given
    Long postId = 1L;
    Integer count = 5;

    // when
    postService.addViewCount(postId, count);

    // then
    PostCountVO postCountVO =
            objectMapper.readValue(redisTemplate.opsForValue().get(String.format("postCount::%s", postId)), PostCountVO.class);
    assertThat(postCountVO.getViewCount()).isSameAs(105);
  }

  //    @Test
  //    @DisplayName("게시글 등록")
  //    void createPost() {
  //      // given
  //      Long memberId = 1L;
  //      CreatePostRequest createPostRequest =
  //          CreatePostRequest.builder()
  //              .title("post title")
  //              .description("post description")
  //              .stature(180.0)
  //              .weight(80.0)
  //              .hashTagNames(List.of("태그 1", "태그 2", "태그 3"))
  //              .postThumbnailImgName("example.png")
  //              .postImgName("example.png")
  //              .postImageProductDetails(
  //                  List.of(
  //                      CreatePostImageProductDetailRequest.builder()
  //                          .productId(1L)
  //                          .productSize("XL")
  //                          .leftGapPercent(40.0)
  //                          .topGapPercent(30.0)
  //                          .build()))
  //              .build();
  //
  //      // when
  //      CreatePostResponse createPostResponse = postService.createPost(memberId,
  // createPostRequest);
  //
  //      // then
  //      assertThat(createPostResponse.getThumbnailImgPreSignedUrl()).isNotEmpty();
  //      assertThat(createPostResponse.getImgPreSignedUrl()).isNotEmpty();
  //    }
}
