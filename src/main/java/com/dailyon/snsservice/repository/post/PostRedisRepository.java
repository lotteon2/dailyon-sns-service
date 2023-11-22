package com.dailyon.snsservice.repository.post;

import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  @Cacheable(value = "postCount", key = "#productId", unless = "#result == null")
  public PostCountVO findPostCountVO(String productId) throws JsonProcessingException {
    String stringValue = redisTemplate.opsForValue().get(productId);
    if(isInValidValue(stringValue)) {
      return null;
    }
    return objectMapper.readValue(stringValue, PostCountVO.class);
  }

  public void putPostCountVO(String key, PostCountVO postCountVO) throws JsonProcessingException {
    String stringValue = objectMapper.writeValueAsString(postCountVO);
    redisTemplate.opsForValue().set(key, stringValue);
  }

  private Boolean isInValidValue(String value) {
    return value == null;
  }
}
