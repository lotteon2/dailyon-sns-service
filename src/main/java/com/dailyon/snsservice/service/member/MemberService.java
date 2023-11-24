package com.dailyon.snsservice.service.member;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public OOTDMemberProfileResponse getOOTDMemberProfile(Long memberId, Long followerId) {
    return memberRepository.findOOTDMemberProfile(memberId, followerId);
  }
}
