package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostServiceTest {

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
    assertSame(8, postPageResponse.getPosts().size());
    postPageResponse.getPosts().forEach(p -> assertNull(p.getIsLike()));
    assertTrue(postPageResponse.getHasNext());
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
    assertSame(8, postPageResponse.getPosts().size());
    postPageResponse.getPosts().forEach(p -> assertNotNull(p.getIsLike()));
    assertTrue(postPageResponse.getHasNext());
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

  //  @Test
  //  @DisplayName("게시글 등록")
  //  void createPost() {
  //    // given
  //    Long memberId = 1L;
  //    CreatePostRequest createPostRequest = CreatePostRequest.builder()
  //            .title("post title")
  //            .description("post description")
  //            .stature(180.0)
  //            .weight(80.0)
  //            .hashTagNames(List.of("태그 1", "태그 2", "태그 3"))
  //            .postThumbnailImgName("thumbnail-img.png")
  //            .postImgName("img.png")
  //            .postImageProductDetails(List.of(CreatePostImageProductDetailRequest.builder()
  //                    .productId(1L)
  //                    .productSize("XL")
  //                    .leftGapPercent(40.0)
  //                    .topGapPercent(30.0)
  //                    .build()))
  //            .build();
  //
  //    // when
  //    CreatePostResponse createPostResponse = postService.createPost(memberId, createPostRequest);
  //
  //    // then
  //    assertFalse(createPostResponse.getThumbnailImgPreSignedUrl().isEmpty());
  //    assertFalse(createPostResponse.getImgPreSignedUrl().isEmpty());
  //  }
}
