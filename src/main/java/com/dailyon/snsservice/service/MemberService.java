package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public FollowingPageResponse getFollowings(Long followerId, Pageable pageable) {
    Page<Member> followings = memberRepository.findFollowingsByFollowerId(followerId, pageable);
    return FollowingPageResponse.fromEntity(followings);
  }
}
