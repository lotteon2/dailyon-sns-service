package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;

public interface MemberRepository {

  OOTDMemberProfileResponse findOOTDMemberProfile(Long memberId, Long followerId);
}
