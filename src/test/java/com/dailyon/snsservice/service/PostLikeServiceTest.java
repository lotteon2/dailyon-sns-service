package com.dailyon.snsservice.service;

import com.dailyon.snsservice.entity.PostLike;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import com.dailyon.snsservice.repository.postlike.PostLikeJpaRepository;
import com.dailyon.snsservice.service.postlike.PostLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostLikeServiceTest {

  @Autowired
  private PostLikeService postLikeService;

  @Autowired
  private PostLikeJpaRepository postLikeJpaRepository;

  @Test
  @DisplayName("게시글 좋아요 추가")
  void createPostLike() {
    // given
    Long memberId = 1L;
    Long postId = 4L;

    // when
    postLikeService.togglePostLike(memberId, postId);

    // then
    Optional<PostLike> postLike = postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
    assertNotNull(postLike.orElse(null));
  }

  @Test
  @DisplayName("게시글 좋아요 삭제")
  void deletePostLike() {
    // given
    Long memberId = 1L;
    Long postId = 2L;

    // when
    postLikeService.togglePostLike(memberId, postId);

    // then
    Optional<PostLike> postLike = postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
    assertNull(postLike.orElse(null));
  }
}
