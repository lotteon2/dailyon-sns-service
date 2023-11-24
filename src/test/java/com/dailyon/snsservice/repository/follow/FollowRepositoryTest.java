package com.dailyon.snsservice.repository.follow;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.Follow;
import com.dailyon.snsservice.entity.ids.FollowId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class FollowRepositoryTest {

  @Autowired private FollowRepository followRepository;

  @Autowired private FollowJpaRepository followJpaRepository;

  @Test
  @DisplayName("팔로우 추가")
  void createFollow() {
    // given
    Long followerId = 1L;
    Long followingId = 4L;

    // when
    followRepository.toggleFollow(followerId, followingId);

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
    followRepository.toggleFollow(followerId, followingId);

    // then
    Optional<Follow> follow = followJpaRepository.findById(new FollowId(followerId, followingId));
    assertNull(follow.orElse(null));
  }

  @Test
  @DisplayName("팔로잉 목록 조회")
  void findFollowingsByMemberId() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<Follow> follows = followRepository.findFollowingsByMemberId(memberId, pageRequest);

    // then
    assertThat(follows.getTotalPages()).isSameAs(1);
    assertThat(follows.getTotalElements()).isSameAs(2L);
    follows
        .getContent()
        .forEach(
            follow -> {
              assertThat(follow.getFollower().getId()).isSameAs(memberId);
              assertThat(follow.getFollowing().getId()).isNotSameAs(memberId);
            });
  }

  @Test
  @DisplayName("팔로워 목록 조회")
  void findFollowersByMemberId() {
    // given
    Long memberId = 2L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<FollowerResponse> followerResponses =
        followRepository.findFollowersByMemberId(memberId, pageRequest);

    // then
    assertSame(1, followerResponses.getTotalPages());
    assertSame(2L, followerResponses.getTotalElements());
    followerResponses.getContent().forEach(followerResponse -> {
      if(followerResponse.getId().equals(1L)) {
        assertThat(followerResponse.getIsFollowing()).isTrue();
      }
    });
  }
}
