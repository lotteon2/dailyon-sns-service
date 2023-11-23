package com.dailyon.snsservice.scheduler;

import com.dailyon.snsservice.repository.post.PostRedisRepository;
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
  private final PostRedisRepository postRedisRepository;

  @Transactional
  @Scheduled(cron = "0 30 * * * *", zone = "Asia/Seoul")
  public void postCountCacheSyncToDB() {
    List<Map<String, PostCountVO>> postCountVOStore =
        postRedisRepository.findPostCountVOs("postCount");
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
