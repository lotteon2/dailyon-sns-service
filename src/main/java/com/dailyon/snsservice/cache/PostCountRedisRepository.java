package com.dailyon.snsservice.cache;

import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCountRedisRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  @Cacheable(value = "postCount", key = "#key", unless = "#result == null")
  public PostCountVO findOrPutPostCountVO(String key, PostCountVO postCountVO)
      throws JsonProcessingException {
    String stringValue = redisTemplate.opsForValue().get(key);
    if (isInValidValue(stringValue)) {
      return postCountVO;
    }
    return objectMapper.readValue(stringValue, PostCountVO.class);
  }

  @CachePut(value = "postCount", key = "#key")
  public PostCountVO modifyPostCountVOAboutLikeCount(String key, PostCountVO postCountVO)
      throws JsonProcessingException {
    return postCountVO;
  }

  @CacheEvict(value = "postCount", key = "#key")
  public void deletePostCountVO(String key) {}

  public List<Map<String, PostCountVO>> findPostCountVOs(String cacheName) {
    Set<String> postIds = redisTemplate.keys(cacheName + ":*");
    if (postIds != null && !postIds.isEmpty()) {
      return postIds.stream()
          .map(
              postId -> {
                String stringValue = redisTemplate.opsForValue().get(postId);
                PostCountVO postCountVO;
                try {
                  postCountVO = objectMapper.readValue(stringValue, PostCountVO.class);
                } catch (JsonProcessingException e) {
                  throw new RuntimeException(e);
                }
                return Map.of(postId, postCountVO);
              })
          .collect(Collectors.toList());
    }
    return null;
  }

  private Boolean isInValidValue(String value) {
    return value == null;
  }
}
