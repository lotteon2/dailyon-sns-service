package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.follow.FollowerPageResponse;
import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
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

  public FollowerPageResponse getFollowers(Long followerId, Pageable pageable) {
    Page<FollowerResponse> followerResponses =
        memberRepository.findFollowersByFollowingId(followerId, pageable);
    return FollowerPageResponse.builder()
        .totalPages(followerResponses.getTotalPages())
        .totalElements(followerResponses.getTotalElements())
        .followers(followerResponses.getContent())
        .build();
  }

  public OOTDMemberProfileResponse getOOTDMemberProfile(Long memberId, Long followerId) {
    return memberRepository.findOOTDMemberProfile(memberId, followerId);
  }
}
