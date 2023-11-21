package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.dto.response.post.OOTDPostPageResponse;
import com.dailyon.snsservice.service.MemberService;
import java.util.Map;

import com.dailyon.snsservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;
  private final PostService postService;

  @GetMapping("/{memberId}")
  public ResponseEntity<Map<String, OOTDMemberProfileResponse>> getOOTDMemberProfile(
      @RequestHeader(name = "memberId") Long followerId,
      @PathVariable(name = "memberId") Long memberId) {
    OOTDMemberProfileResponse ootdMemberProfileResponse =
        memberService.getOOTDMemberProfile(memberId, followerId);
    return ResponseEntity.ok(Map.of("member", ootdMemberProfileResponse));
  }

  @GetMapping("/{memberId}/posts")
  public ResponseEntity<OOTDPostPageResponse> getOOTDPosts(
          @RequestHeader(name = "memberId") Long memberId,
          @PathVariable(name = "memberId") Long targetId,
          @PageableDefault(
                  page = 0,
                  size = 8,
                  sort = {"createdAt"},
                  direction = Sort.Direction.DESC)
          Pageable pageable) {
    OOTDPostPageResponse ootdPostPageResponse = postService.getOOTDPosts(targetId, pageable);
    return ResponseEntity.ok(ootdPostPageResponse);
  }
}
