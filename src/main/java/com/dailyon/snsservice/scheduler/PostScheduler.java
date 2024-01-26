package com.dailyon.snsservice.scheduler;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.repository.post.PostRepository;
import com.dailyon.snsservice.vo.PostCountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostScheduler {

  private final PostRepository postRepository;
  private final PostCountRedisRepository postCountRedisRepository;

  @Transactional
  @Scheduled(cron = "0 5 * * * *", zone = "Asia/Seoul")
  public void postCountCacheSyncToDB() {
    List<Map<String, PostCountVO>> postCountVOStore =
        postCountRedisRepository.findPostCountVOs();
    if (postCountVOStore != null) {
      postCountVOStore.forEach(
          store ->
              store.forEach(
                  (key, postCountVO) ->
                      postRepository.updateCountsById(
                          Long.parseLong(key.split("::")[1]),
                          postCountVO.getViewCount(),
                          postCountVO.getLikeCount(),
                          postCountVO.getCommentCount())));
    }
  }
}
