package com.dailyon.snsservice.repository.follow;

public interface FollowRepository {

  void toggleFollow(Long followerId, Long followingId);
}
