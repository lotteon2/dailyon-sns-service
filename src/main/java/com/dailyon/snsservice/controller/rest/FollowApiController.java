package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/follows")
@RestController
@RequiredArgsConstructor
public class FollowApiController {

  private final FollowService followService;

  @PutMapping("/{followingId}")
  public ResponseEntity<Void> toggleFollow(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "followingId") Long followingId) {
    followService.toggleFollow(memberId, followingId);
    return ResponseEntity.ok().build();
  }
}
