package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public Page<Member> findFollowingsByFollowerId(Long followingId, Pageable pageable) {
    return memberJpaRepository.findFollowingsByFollowerId(followingId, pageable);
  }
}
