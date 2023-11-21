package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.response.follow.FollowerPageResponse;
import com.dailyon.snsservice.dto.response.follow.FollowingPageResponse;
import com.dailyon.snsservice.service.FollowService;
import com.dailyon.snsservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/follows")
@RestController
@RequiredArgsConstructor
public class FollowApiController {

  private final FollowService followService;
  private final MemberService memberService;

  @GetMapping("/followings")
  public ResponseEntity<FollowingPageResponse> getFollowings(
      @RequestHeader(name = "memberId") Long memberId,
      @PageableDefault(
              page = 0,
              size = 5,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    FollowingPageResponse followingPageResponse = memberService.getFollowings(memberId, pageable);
    return ResponseEntity.ok(followingPageResponse);
  }

  @GetMapping("/followers")
  public ResponseEntity<FollowerPageResponse> getFollowers(
      @RequestHeader(name = "memberId") Long memberId,
      @PageableDefault(
              page = 0,
              size = 5,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    FollowerPageResponse followerPageResponse = memberService.getFollowers(memberId, pageable);
    return ResponseEntity.ok(followerPageResponse);
  }

  @PutMapping("/{followingId}")
  public ResponseEntity<Void> toggleFollow(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "followingId") Long followingId) {
    followService.toggleFollow(memberId, followingId);
    return ResponseEntity.ok().build();
  }
}
