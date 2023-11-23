package com.dailyon.snsservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.PostLike;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import com.dailyon.snsservice.repository.postlike.PostLikeJpaRepository;
import com.dailyon.snsservice.service.postlike.PostLikeService;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostLikeServiceTest {

  @Autowired private PostLikeService postLikeService;

  @Autowired private PostLikeJpaRepository postLikeJpaRepository;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("게시글 좋아요 추가")
  void createPostLike() throws JsonProcessingException {
    // given
    Long memberId = 1L;
    Long postId = 4L;

    // when
    postLikeService.togglePostLike(memberId, postId);

    // then
    Optional<PostLike> postLike = postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
    String stringValue = redisTemplate.opsForValue().get(String.format("postCount::%s", postId));
    PostCountVO postCountVO = objectMapper.readValue(stringValue, PostCountVO.class);

    assertThat(postLike.orElse(null)).isNotNull();
    assertThat(postCountVO.getLikeCount()).isSameAs(41);
  }

  @Test
  @DisplayName("게시글 좋아요 삭제")
  void deletePostLike() throws JsonProcessingException {
    // given
    Long memberId = 1L;
    Long postId = 2L;

    // when
    postLikeService.togglePostLike(memberId, postId);

    // then
    Optional<PostLike> postLike = postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
    String stringValue = redisTemplate.opsForValue().get(String.format("postCount::%s", postId));
    PostCountVO postCountVO = objectMapper.readValue(stringValue, PostCountVO.class);

    assertThat(postLike.orElse(null)).isNull();
    assertThat(postCountVO.getLikeCount()).isSameAs(44);
  }
}
