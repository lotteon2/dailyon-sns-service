package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.service.MemberService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @GetMapping("/{memberId}")
  public ResponseEntity<Map<String, OOTDMemberProfileResponse>> getOOTDMemberProfile(
      @RequestHeader(name = "memberId") Long followerId,
      @PathVariable(name = "memberId") Long memberId) {
    OOTDMemberProfileResponse ootdMemberProfileResponse =
        memberService.getOOTDMemberProfile(memberId, followerId);
    return ResponseEntity.ok(Map.of("member", ootdMemberProfileResponse));
  }
}
