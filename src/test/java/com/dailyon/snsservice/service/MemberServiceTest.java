package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.follow.FollowerPageResponse;
import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class MemberServiceTest {

  @Autowired private MemberService memberService;

  @Test
  @DisplayName("팔로잉 목록 조회")
  void getFollowings() {
    // given
    Long followerId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    FollowingPageResponse followingPageResponse =
        memberService.getFollowings(followerId, pageRequest);

    // then
    assertSame(1, followingPageResponse.getTotalPages());
    assertSame(2L, followingPageResponse.getTotalElements());
    assertSame(2, followingPageResponse.getFollowings().size());
  }

  @Test
  @DisplayName("팔로워 목록 조회")
  void getFollowers() {
    // given
    Long followerId = 2L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    FollowerPageResponse followerPageResponse = memberService.getFollowers(followerId, pageRequest);

    // then
    assertSame(1, followerPageResponse.getTotalPages());
    assertSame(2L, followerPageResponse.getTotalElements());
    assertSame(2, followerPageResponse.getFollowers().size());
  }
}
