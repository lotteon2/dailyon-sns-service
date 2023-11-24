package com.dailyon.snsservice.repository.member;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
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
class MemberRepositoryTest {

  @Autowired private MemberRepository memberRepository;

  @Test
  @DisplayName("팔로워 목록 조회")
  void findFollowersByFollowingId() {
    // given
    Long followingId = 2L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when

    Page<FollowerResponse> followerResponses =
        memberRepository.findFollowersByFollowingId(followingId, pageRequest);

    // then
    assertSame(1, followerResponses.getTotalPages());
    assertSame(2L, followerResponses.getTotalElements());
  }

  @Test
  @DisplayName("OOTD 사용자 프로필 조회 - 팔로잉 O")
  void findOOTDMemberProfile1() {
    // given
    Long memberId = 2L;
    Long followerId = 1L;

    // when

    OOTDMemberProfileResponse ootdMemberProfile =
        memberRepository.findOOTDMemberProfile(memberId, followerId);

    // then
    assertTrue(ootdMemberProfile.getIsFollowing());
  }

  @Test
  @DisplayName("OOTD 사용자 프로필 조회 - 팔로잉 X")
  void findOOTDMemberProfile2() {
    // given
    Long memberId = 1L;
    Long followerId = 3L;

    // when

    OOTDMemberProfileResponse ootdMemberProfile =
        memberRepository.findOOTDMemberProfile(memberId, followerId);

    // then
    assertFalse(ootdMemberProfile.getIsFollowing());
  }
}
