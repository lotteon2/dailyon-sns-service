package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.service.postlike.PostLikeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
    origins = {"http://localhost:5173", "http://127.0.0.1::5173"},
    allowCredentials = "true",
    allowedHeaders = "*")
@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostLikeApiController {

  private final PostLikeService postLikeService;

  @PutMapping("/likes")
  public ResponseEntity<Void> togglePostLike(
      @RequestHeader(name = "memberId") Long memberId,
      @RequestParam(name = "postIds") List<Long> postIds) {
    postLikeService.togglePostLike(memberId, postIds);
    return ResponseEntity.ok().build();
  }
}
