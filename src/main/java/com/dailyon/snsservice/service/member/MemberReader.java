package com.dailyon.snsservice.service.member;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberReader {

  private final MemberJpaRepository memberJpaRepository;

  public Member read(Long memberId) {
    return memberJpaRepository.findById(memberId).orElseThrow(MemberEntityNotFoundException::new);
  }

  public List<Member> readAll(List<Long> memberIds) {
    return memberJpaRepository.findAllById(memberIds);
  }
}
