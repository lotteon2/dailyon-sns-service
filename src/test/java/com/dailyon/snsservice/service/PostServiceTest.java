package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.dto.response.post.PostResponse;
import com.dailyon.snsservice.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

  @PersistenceContext private EntityManager em;

  @Autowired private PostService postService;

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
    assertTrue(postPageResponse.getPosts().get(0).getIsLike());
    assertTrue(postPageResponse.getHasNext());
  }
}
