package com.dailyon.snsservice.service;

import static org.assertj.core.api.Assertions.*;
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
    OOTDMemberProfileResponse ootdMemberProfileResponse =
        memberService.getOOTDMemberProfile(memberId, followerId);

    // then
    assertThat(ootdMemberProfileResponse.getId()).isSameAs(memberId);
    assertThat(ootdMemberProfileResponse.getNickname()).isNotNull();
    assertThat(ootdMemberProfileResponse.getProfileImgUrl()).isNotNull();
    assertThat(ootdMemberProfileResponse.getFollowingCount()).isNotNull();
    assertThat(ootdMemberProfileResponse.getFollowerCount()).isNotNull();
    assertThat(ootdMemberProfileResponse.getIsFollowing()).isTrue();
  }
}
