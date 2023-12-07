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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostLikeServiceTest {

  @Autowired private PostLikeService postLikeService;

  @Autowired private PostLikeJpaRepository postLikeJpaRepository;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Autowired private ObjectMapper objectMapper;

  @PersistenceContext private EntityManager em;

  @Test
  @DisplayName("게시글 좋아요 토글 - 벌크 연산")
  void createPostLike() throws JsonProcessingException {
    // given
    Long memberId = 1L;
    List<Long> postIds = List.of(1L, 2L, 3L, 4L);

    List<PostCountVO> beforePostCountVOs =
        postIds.stream()
            .map(
                postId -> {
                  try {
                    return objectMapper.readValue(
                        redisTemplate.opsForValue().get(String.format("postCount::%s", postId)),
                        PostCountVO.class);
                  } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                  }
                })
            .collect(Collectors.toList());

    // when
    postLikeService.togglePostLike(memberId, postIds);
    em.flush();
    em.clear();

    // then
    postIds.forEach(
        postId -> {
          try {
            Optional<PostLike> postLike =
                postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
            if (postId.equals(2L)) {
              assertThat(postLike.orElse(null)).isNull();
              PostCountVO afterPostCountVO =
                  objectMapper.readValue(
                      redisTemplate.opsForValue().get(String.format("postCount::%s", postId)),
                      PostCountVO.class);

              assertThat(
                      beforePostCountVOs.stream()
                          .anyMatch(
                              beforePostCountVO ->
                                  beforePostCountVO
                                      .getLikeCount()
                                      .equals(afterPostCountVO.getLikeCount() + 1)))
                  .isTrue();
            } else {
              assertThat(postLike.orElse(null)).isNotNull();
              PostCountVO afterPostCountVO =
                  objectMapper.readValue(
                      redisTemplate.opsForValue().get(String.format("postCount::%s", postId)),
                      PostCountVO.class);

              assertThat(
                      beforePostCountVOs.stream()
                          .anyMatch(
                              beforePostCountVO ->
                                  beforePostCountVO
                                      .getLikeCount()
                                      .equals(afterPostCountVO.getLikeCount() - 1)))
                  .isTrue();
            }
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        });
  }
}
