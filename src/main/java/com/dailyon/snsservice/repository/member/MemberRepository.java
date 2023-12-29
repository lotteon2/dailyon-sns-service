package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.entity.Member;

public interface MemberRepository {

  OOTDMemberProfileResponse findOOTDMemberProfile(Long memberId, Long followerId);

  void save(Member member);
}
