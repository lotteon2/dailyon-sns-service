package com.dailyon.snsservice.repository.follow;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepository {

  void toggleFollow(Long followerId, Long followingId);

  Page<Follow> findFollowingsByMemberId(Long memberId, Pageable pageable);

  Page<FollowerResponse> findFollowersByMemberId(Long memberId, Pageable pageable);
}
