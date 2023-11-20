package com.dailyon.snsservice.service;

import com.dailyon.snsservice.repository.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;

  @Transactional
  public void toggleFollow(Long followerId, Long followingId) {
    followRepository.toggleFollow(followerId, followingId);
  }
}
