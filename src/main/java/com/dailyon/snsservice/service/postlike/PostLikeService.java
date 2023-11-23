package com.dailyon.snsservice.service.postlike;

import com.dailyon.snsservice.repository.postlike.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;

  @Transactional
  public void togglePostLike(Long memberId, Long postId) {
    postLikeRepository.togglePostLike(memberId, postId);
  }
}
