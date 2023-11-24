package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.follow.FollowerPageResponse;
import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

  @Test
  @DisplayName("OOTD 사용자 프로필 조회")
  void getOOTDMemberProfile() {
    // given
    Long memberId = 2L;
    Long followerId = 1L;

    // when
    OOTDMemberProfileResponse ootdMemberProfile = memberService.getOOTDMemberProfile(memberId, followerId);

    // then
    assertNotNull(ootdMemberProfile.getId());
    assertNotNull(ootdMemberProfile.getNickname());
    assertNotNull(ootdMemberProfile.getProfileImgUrl());
    assertNotNull(ootdMemberProfile.getFollowingCount());
    assertNotNull(ootdMemberProfile.getFollowerCount());
    assertTrue(ootdMemberProfile.getIsFollowing());
  }
}
