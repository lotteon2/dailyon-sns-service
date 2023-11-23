package com.dailyon.snsservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.post.OOTDPostPageResponse;
import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.dto.response.post.Top4OOTDResponse;
import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.service.post.PostService;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    postPageResponse.getPosts().forEach(p -> assertNull(p.getIsLike()));
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
    postPageResponse.getPosts().forEach(p -> assertNotNull(p.getIsLike()));
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
    assertFalse(postLikePageResponse.getHasNext());
    assertSame(1, postLikePageResponse.getPosts().size());
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
    assertSame(6, ootdPostPageResponse.getPosts().size());
    assertFalse(ootdPostPageResponse.getHasNext());
  }

  @Test
  @DisplayName("OOTD Top4 게시글 목록 조회")
  void getTop4OOTDPosts() {
    // given
    Long productId = 101L;

    // when
    List<Top4OOTDResponse> top4OOTDResponses = postService.getTop4OOTDPosts(productId);

    // then
    assertSame(4, top4OOTDResponses.size());
  }

  //  @Test
  //  @DisplayName("게시글 등록")
  //  void createPost() {
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
  //    // when
  //    CreatePostResponse createPostResponse = postService.createPost(memberId, createPostRequest);
  //
  //    // then
  //    assertThat(createPostResponse.getThumbnailImgPreSignedUrl()).isNotEmpty();
  //    assertThat(createPostResponse.getImgPreSignedUrl()).isNotEmpty();
  //  }
}
