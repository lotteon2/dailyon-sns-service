package com.dailyon.snsservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class MemberServiceTest {

  @Autowired private MemberService memberService;

  @Test
  @DisplayName("OOTD 사용자 프로필 조회")
  void getOOTDMemberProfile() {
    // given
    Long memberId = 2L;
    Long followerId = 1L;

    // when
    OOTDMemberProfileResponse ootdMemberProfile =
        memberService.getOOTDMemberProfile(memberId, followerId);

    // then
    assertNotNull(ootdMemberProfile.getId());
    assertNotNull(ootdMemberProfile.getNickname());
    assertNotNull(ootdMemberProfile.getProfileImgUrl());
    assertNotNull(ootdMemberProfile.getFollowingCount());
    assertNotNull(ootdMemberProfile.getFollowerCount());
    assertTrue(ootdMemberProfile.getIsFollowing());
  }
}
