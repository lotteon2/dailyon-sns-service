package com.dailyon.snsservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.follow.FollowerPageResponse;
import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.entity.Follow;
import com.dailyon.snsservice.entity.ids.FollowId;
import com.dailyon.snsservice.repository.follow.FollowJpaRepository;
import com.dailyon.snsservice.service.follow.FollowService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class FollowServiceTest {

  @Autowired private FollowService followService;

  @Autowired private FollowJpaRepository followJpaRepository;

  @Test
  @DisplayName("팔로우 추가")
  void createFollow() {
    // given
    Long followerId = 1L;
    Long followingId = 4L;

    // when
    followService.toggleFollow(followerId, followingId);

    // then
    Optional<Follow> follow = followJpaRepository.findById(new FollowId(followerId, followingId));
    assertNotNull(follow.orElse(null));
  }

  @Test
  @DisplayName("팔로우 삭제")
  void deleteFollow() {
    // given
    Long followerId = 1L;
    Long followingId = 2L;

    // when
    followService.toggleFollow(followerId, followingId);

    // then
    Optional<Follow> follow = followJpaRepository.findById(new FollowId(followerId, followingId));
    assertNull(follow.orElse(null));
  }

  @Test
  @DisplayName("팔로잉 목록 조회")
  void getFollowings() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    FollowingPageResponse followingPageResponse =
        followService.getFollowings(memberId, pageRequest);

    // then
    assertThat(followingPageResponse.getTotalPages()).isSameAs(1);
    assertThat(followingPageResponse.getTotalElements()).isSameAs(2L);
    assertThat(followingPageResponse.getFollowings().size()).isSameAs(2);
  }

  @Test
  @DisplayName("팔로워 목록 조회")
  void getFollowers() {
    // given
    Long memberId = 2L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    FollowerPageResponse followerPageResponse = followService.getFollowers(memberId, pageRequest);

    // then
    assertThat(followerPageResponse.getTotalPages()).isSameAs(1);
    assertThat(followerPageResponse.getTotalElements()).isSameAs(2L);
    assertThat(followerPageResponse.getFollowers().size()).isSameAs(2);
    followerPageResponse
        .getFollowers()
        .forEach(
            followerResponse -> {
              if (followerResponse.getId().equals(1L)) {
                assertThat(followerResponse.getIsFollowing()).isTrue();
              }
            });
  }
}
