package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public Page<Member> findFollowingsByFollowerId(Long followerId, Pageable pageable) {
    return memberJpaRepository.findFollowingsByFollowerId(followerId, pageable);
  }

  @Override
  public Page<FollowerResponse> findFollowersByFollowingId(Long followingId, Pageable pageable) {
    return memberJpaRepository.findFollowersByFollowingId(followingId, pageable);
  }

  @Override
  public OOTDMemberProfileResponse findOOTDMemberProfile(Long memberId, Long followerId) {
    return memberJpaRepository
        .findOOTDMemberProfile(memberId, followerId)
        .orElseThrow(MemberEntityNotFoundException::new);
  }
}
