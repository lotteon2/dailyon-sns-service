package com.dailyon.snsservice.service.follow;

import com.dailyon.snsservice.dto.response.follow.FollowerPageResponse;
import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.entity.Follow;
import com.dailyon.snsservice.repository.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;

  @Transactional
  public void toggleFollow(Long followerId, List<Long> followingIds) {
    followRepository.toggleFollow(followerId, followingIds);
  }

  public FollowingPageResponse getFollowings(Long memberId, Pageable pageable) {
    Page<Follow> follows = followRepository.findFollowingsByMemberId(memberId, pageable);
    return FollowingPageResponse.fromEntity(follows);
  }

  public FollowerPageResponse getFollowers(Long memberId, Pageable pageable) {
    Page<FollowerResponse> followerResponses =
            followRepository.findFollowersByMemberId(memberId, pageable);
    return FollowerPageResponse.fromDto(followerResponses);
  }
}
