package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

  Page<Member> findFollowingsByFollowerId(Long followerId, Pageable pageable);

  Page<FollowerResponse> findFollowersByFollowingId(Long followingId, Pageable pageable);
}
