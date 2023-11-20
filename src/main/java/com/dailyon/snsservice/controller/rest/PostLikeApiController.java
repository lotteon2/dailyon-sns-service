package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostLikeApiController {

  private final PostLikeService postLikeService;

  @PutMapping("/{postId}/likes")
  public ResponseEntity<Void> togglePostLike(
      @RequestHeader(name = "memberId") Long memberId, @PathVariable(name = "postId") Long postId) {
    postLikeService.togglePostLike(memberId, postId);
    return ResponseEntity.ok().build();
  }
}
