package com.dailyon.snsservice.repository.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class MemberRepositoryTest {

  @Autowired private MemberRepository memberRepository;

  @Test
  @DisplayName("OOTD 사용자 프로필 조회 - 팔로잉 O")
  void findOOTDMemberProfile1() {
    // given
    Long memberId = 2L;
    Long followerId = 1L;

    // when

    OOTDMemberProfileResponse ootdMemberProfileResponse =
        memberRepository.findOOTDMemberProfile(memberId, followerId);

    // then
    assertThat(ootdMemberProfileResponse.getId()).isSameAs(memberId);
    assertThat(ootdMemberProfileResponse.getNickname()).isNotNull();
    assertThat(ootdMemberProfileResponse.getProfileImgUrl()).isNotNull();
    assertThat(ootdMemberProfileResponse.getFollowingCount()).isNotNull();
    assertThat(ootdMemberProfileResponse.getFollowerCount()).isNotNull();
    assertThat(ootdMemberProfileResponse.getIsFollowing()).isTrue();
  }

  @Test
  @DisplayName("OOTD 사용자 프로필 조회 - 팔로잉 X")
  void findOOTDMemberProfile2() {
    // given
    Long memberId = 1L;
    Long followerId = 3L;

    // when

    OOTDMemberProfileResponse ootdMemberProfileResponse =
        memberRepository.findOOTDMemberProfile(memberId, followerId);

    // then
    assertThat(ootdMemberProfileResponse.getId()).isSameAs(memberId);
    assertThat(ootdMemberProfileResponse.getNickname()).isNotNull();
    assertThat(ootdMemberProfileResponse.getProfileImgUrl()).isNotNull();
    assertThat(ootdMemberProfileResponse.getFollowingCount()).isNotNull();
    assertThat(ootdMemberProfileResponse.getFollowerCount()).isNotNull();
    assertThat(ootdMemberProfileResponse.getIsFollowing()).isFalse();
  }
}
