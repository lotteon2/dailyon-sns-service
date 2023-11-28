package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.request.comment.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.comment.CreateReplyCommentRequest;
import com.dailyon.snsservice.dto.response.comment.CommentPageResponse;
import com.dailyon.snsservice.service.comment.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class CommentApiController {

  private final CommentService commentService;

  @GetMapping("/{postId}/comments")
  public ResponseEntity<CommentPageResponse> getComments(
          @RequestHeader(name = "memberId") Long memberId,
          @PathVariable(name = "postId") Long postId,
          @PageableDefault(
                  page = 0,
                  size = 5,
                  sort = {"createdAt"},
                  direction = Sort.Direction.DESC)
          Pageable pageable) {
    CommentPageResponse commentPageResponse = commentService.getComments(postId, pageable);
    return ResponseEntity.ok(commentPageResponse);
  }

  @PostMapping("/{postId}/comments")
  public ResponseEntity<Void> createComment(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "postId") Long postId,
      @Valid @RequestBody CreateCommentRequest createCommentRequest) {
    commentService.createComment(memberId, postId, createCommentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<Void> createReplyComment(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "postId") Long postId,
      @PathVariable(name = "commentId") Long commentId,
      @Valid @RequestBody CreateReplyCommentRequest createReplyCommentRequest) {
    commentService.createReplyComment(memberId, postId, commentId, createReplyCommentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable(name = "postId") Long postId,
      @PathVariable(name = "commentId") Long commentId) {
    commentService.softDeleteComment(commentId, postId, memberId);
    return ResponseEntity.ok().build();
  }
}
