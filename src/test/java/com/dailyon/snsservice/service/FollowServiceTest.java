package com.dailyon.snsservice.service;

import com.dailyon.snsservice.entity.Follow;
import com.dailyon.snsservice.entity.PostLike;
import com.dailyon.snsservice.entity.ids.FollowId;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import com.dailyon.snsservice.repository.follow.FollowJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class FollowServiceTest {

  @Autowired
  private FollowService followService;

  @Autowired
  private FollowJpaRepository followJpaRepository;

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
}
