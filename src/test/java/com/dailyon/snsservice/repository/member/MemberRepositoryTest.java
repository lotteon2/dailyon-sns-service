package com.dailyon.snsservice.repository.member;

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
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("팔로잉 목록 조회")
  void findFollowingsByFollowerId() {
    // given
    Long followerId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<Member> followings = memberRepository.findFollowingsByFollowerId(followerId, pageRequest);

    // then
    assertSame(1, followings.getTotalPages());
    assertSame(2L, followings.getTotalElements());
  }
}
