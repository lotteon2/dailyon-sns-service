package com.dailyon.snsservice.cache;

import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCountRedisRepository {

  private final RedisTemplate<String, PostCountVO> redisTemplate;

  @Cacheable(value = "postCount", key = "#key", unless = "#result == null")
  public PostCountVO findOrPutPostCountVO(String key, PostCountVO postCountVO)
      throws JsonProcessingException {
    PostCountVO cachedPostCountVO = redisTemplate.opsForValue().get(key);
    if (isInValidValue(cachedPostCountVO)) {
      return postCountVO;
    }
    return cachedPostCountVO;
  }

  @CachePut(value = "postCount", key = "#key")
  public PostCountVO modifyPostCountVOAboutLikeCount(String key, PostCountVO postCountVO)
      throws JsonProcessingException {
    return postCountVO;
  }

  @CacheEvict(value = "postCount", key = "#key")
  public void deletePostCountVO(String key) {}

  @Cacheable(value = "postCount", unless = "#result == null", key = "'*'")
  public List<Map<String, PostCountVO>> findPostCountVOs() {
    Set<String> allKeys = redisTemplate.keys("postCount::*");

    // Fetch values for each key
    List<Map<String, PostCountVO>> allValues =
        allKeys.stream()
            .map(
                key -> {
                  String postId = key.split("::")[1];
                  PostCountVO cachedPostCountVO = redisTemplate.opsForValue().get(key);
                  if (isInValidValue(cachedPostCountVO)) {
                    return Map.of(postId, new PostCountVO(0, 0, 0));
                  }
                  return Map.of(postId, cachedPostCountVO);
                })
            .collect(Collectors.toList());

    return allValues;
  }

  private Boolean isInValidValue(PostCountVO value) {
    return value == null;
  }
}
