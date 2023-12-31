package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.service.member.MemberService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  @GetMapping("/{ootdMemberId}")
  public ResponseEntity<Map<String, OOTDMemberProfileResponse>> getOOTDMemberProfile(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "ootdMemberId") Long ootdMemberId) {
    OOTDMemberProfileResponse ootdMemberProfileResponse =
        memberService.getOOTDMemberProfile(ootdMemberId, memberId);
    return ResponseEntity.ok(Map.of("member", ootdMemberProfileResponse));
  }
}
