package com.dailyon.snsservice.cache;

import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.repository.post.PostRepository;
import com.dailyon.snsservice.vo.Top4OOTDVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class Top4OOTDRedisRepository {

  private final PostRepository postRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  @Cacheable(value = "top4OOTD", key = "#productId", unless = "#result == null")
  public List<Top4OOTDVO> findOrPutTop4OOTDVO(String productId) throws JsonProcessingException {
    String stringValue = redisTemplate.opsForValue().get(productId);
    if (isInValidValue(stringValue)) {
      List<Post> posts = postRepository.findTop4ByOrderByLikeCountDesc(Long.parseLong(productId));
      return posts.stream()
          .map(post -> new Top4OOTDVO(post.getId(), post.getPostImage().getThumbnailImgUrl()))
          .collect(Collectors.toList());
    }
    return objectMapper.readValue(
        stringValue,
        objectMapper.getTypeFactory().constructCollectionType(List.class, Top4OOTDVO.class));
  }

  private Boolean isInValidValue(String value) {
    return value == null;
  }
}
