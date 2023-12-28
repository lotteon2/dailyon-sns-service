package com.dailyon.snsservice.config;

import com.dailyon.snsservice.vo.PostCountVO;
import com.dailyon.snsservice.vo.Top4OOTDVO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class CacheConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module());
    return objectMapper;
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(600))
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(objectMapper)));
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
      ObjectMapper objectMapper) {
    Jackson2JsonRedisSerializer<PostCountVO> postCountVOJackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(PostCountVO.class);

    JavaType top4OOTDVOListType =
        objectMapper.getTypeFactory().constructCollectionType(List.class, Top4OOTDVO.class);
    Jackson2JsonRedisSerializer<List<Top4OOTDVO>> top4OOTDVOJackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(top4OOTDVOListType);

    postCountVOJackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    top4OOTDVOJackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    return (builder ->
        builder
            .withCacheConfiguration(
                "postCount",
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofDays(1))
                    .disableCachingNullValues()
                    .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            new StringRedisSerializer()))
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            postCountVOJackson2JsonRedisSerializer)))
            .withCacheConfiguration(
                "top4OOTD",
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofDays(1))
                    .disableCachingNullValues()
                    .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            new StringRedisSerializer()))
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            top4OOTDVOJackson2JsonRedisSerializer))));
  }
}
