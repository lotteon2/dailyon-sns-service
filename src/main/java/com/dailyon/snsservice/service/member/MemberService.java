package com.dailyon.snsservice.service.member;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.repository.member.MemberRepository;
import dailyon.domain.sns.kafka.dto.MemberCreateDTO;
import dailyon.domain.sns.kafka.dto.MemberUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberReader memberReader;

  public OOTDMemberProfileResponse getOOTDMemberProfile(Long ootdMemberId, Long memberId) {
    return memberRepository.findOOTDMemberProfile(ootdMemberId, memberId);
  }

  public void createMember(MemberCreateDTO memberCreateDTO) {
    Member member =
        Member.createMember(
            memberCreateDTO.getNickname(),
            memberCreateDTO.getProfileImgUrl(),
            memberCreateDTO.getCode());
    memberRepository.save(member);
  }

  public void updateMember(MemberUpdateDTO memberUpdateDTO) {
    Member member = memberReader.read(memberUpdateDTO.getId());
    member.updateMember(memberUpdateDTO.getNickname(), memberUpdateDTO.getProfileImgUrl());
  }
}
