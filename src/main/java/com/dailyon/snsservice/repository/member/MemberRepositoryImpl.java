package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

  private final MemberJpaRepository memberJpaRepository;

  @Override
  public OOTDMemberProfileResponse findOOTDMemberProfile(Long ootdMemberId, Long memberId) {
    return memberJpaRepository
        .findOOTDMemberProfile(ootdMemberId, memberId)
        .orElseThrow(MemberEntityNotFoundException::new);
  }
}
