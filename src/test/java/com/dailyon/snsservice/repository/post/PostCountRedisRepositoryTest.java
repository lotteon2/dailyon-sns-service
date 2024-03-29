package com.dailyon.snsservice.repository.post;

import static org.assertj.core.api.Assertions.*;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.dto.response.post.PostResponse;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostCountRedisRepositoryTest {

  @Autowired private PostCountRedisRepository postCountRedisRepository;
  @Autowired private PostRepository postRepository;
  @Autowired private RedisTemplate<String, PostCountVO> redisTemplate;

  @Test
  @DisplayName("캐시 miss시 DB에서 캐시로 동기화 후 반환, 캐시 hit시 조회수, 좋아요수, 댓글수를 조회")
  void findOrPutPostCountVO() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    Page<PostResponse> postResponses = postRepository.findAllWithIsLike(memberId, pageRequest);

    // then
    postResponses
        .getContent()
        .forEach(
            postResponse -> {
              try {
                PostCountVO DBPostCountVO =
                    new PostCountVO(
                        postResponse.getViewCount(),
                        postResponse.getLikeCount(),
                        postResponse.getCommentCount());
                PostCountVO cachedPostCountVO =
                    postCountRedisRepository.findOrPutPostCountVO(
                        String.valueOf(postResponse.getId()), DBPostCountVO);
                assertThat(cachedPostCountVO.getViewCount()).isNotNull();
                assertThat(cachedPostCountVO.getLikeCount()).isNotNull();
                assertThat(cachedPostCountVO.getCommentCount()).isNotNull();
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });
  }

  @Test
  @DisplayName("postCount에 해당하는 캐시 이름으로 캐시 모두 조회")
  void findPostCountVOs() {
    // given
    String cacheName = "postCount";
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));
    Page<PostResponse> postResponses = postRepository.findAllWithIsLike(memberId, pageRequest);
    postResponses
        .getContent()
        .forEach(
            postResponse -> {
              try {
                PostCountVO DBPostCountVO =
                    new PostCountVO(
                        postResponse.getViewCount(),
                        postResponse.getLikeCount(),
                        postResponse.getCommentCount());
                postCountRedisRepository.findOrPutPostCountVO(
                    String.valueOf(postResponse.getId()), DBPostCountVO);

              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });

    // when
    List<Map<String, PostCountVO>> postCountVOStore =
        postCountRedisRepository.findPostCountVOs();

    // then
    assertThat(postCountVOStore.size()).isNotSameAs(0);
    postCountVOStore.forEach(
        postCountVOMap ->
            postCountVOMap.forEach(
                (key, postCountVO) -> {
                  assertThat(postCountVO.getViewCount()).isNotNull();
                  assertThat(postCountVO.getLikeCount()).isNotNull();
                  assertThat(postCountVO.getCommentCount()).isNotNull();
                }));
  }

  @Test
  @DisplayName("캐시에서 게시글에 대한 조회수, 좋아요수, 댓글수 삭제")
  void deletePostCountVO() throws JsonProcessingException {
    // given
    Long postId = 1L;
    PostCountVO postCountVO = new PostCountVO(1, 2, 3);
    redisTemplate.opsForValue().set(String.format("postCount::%s", postId), postCountVO);

    // when
    postCountRedisRepository.deletePostCountVO(String.valueOf(postId));

    // then
    assertThat(redisTemplate.opsForValue().get(String.format("postCount::%s", postId))).isNull();
  }
}
