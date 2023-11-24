package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

  Page<FollowerResponse> findFollowersByFollowingId(Long followingId, Pageable pageable);

  OOTDMemberProfileResponse findOOTDMemberProfile(Long memberId, Long followerId);
}
