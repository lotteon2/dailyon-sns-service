package com.dailyon.snsservice.repository.follow;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
  void findAllByMemberId() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<Follow> followings = followRepository.findFollowingsByMemberId(memberId, pageRequest);

    // then
    assertThat(followings.getTotalPages()).isSameAs(1);
    assertThat(followings.getTotalElements()).isSameAs(2L);
    followings
        .getContent()
        .forEach(
            following -> {
              assertThat(following.getFollower().getId()).isSameAs(memberId);
              assertThat(following.getFollowing().getId()).isNotSameAs(memberId);
            });
  }
}
