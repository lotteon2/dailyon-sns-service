package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.request.post.CreateCommentRequest;
import javax.validation.Valid;

import com.dailyon.snsservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class CommentApiController {

  private final CommentService commentService;

  @PostMapping("/{postId}/comments")
  public ResponseEntity<Void> createComment(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "postId") Long postId,
      @Valid @RequestBody CreateCommentRequest createCommentRequest) {
    commentService.createComment(memberId, postId, createCommentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
