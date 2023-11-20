package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.dto.request.post.UpdatePostRequest;
import com.dailyon.snsservice.dto.response.post.CreatePostResponse;
import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.dto.response.post.UpdatePostResponse;
import com.dailyon.snsservice.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<CreatePostResponse> createPost(
      @RequestHeader(name = "memberId") Long memberId,
      @Valid @RequestBody CreatePostRequest createPostRequest) {
    CreatePostResponse createPostResponse = postService.createPost(memberId, createPostRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createPostResponse);
  }

  @GetMapping
  public ResponseEntity<PostPageResponse> getPosts(
      @RequestHeader(name = "memberId", required = false) Long memberId,
      @PageableDefault(
              page = 0,
              size = 8,
              sort = {"viewCount"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    PostPageResponse postPageResponse = postService.getPosts(memberId, pageable);
    return ResponseEntity.ok(postPageResponse);
  }

  @PutMapping("/{postId}")
  public ResponseEntity<UpdatePostResponse> updatePost(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable("postId") Long postId,
      @Valid @RequestBody UpdatePostRequest updatePostRequest) {
    UpdatePostResponse updatePostResponse = postService.updatePost(postId, updatePostRequest);
    return ResponseEntity.ok(updatePostResponse);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(
      @RequestHeader(name = "memberId") Long memberId, @PathVariable("postId") Long postId) {
    postService.softDeletePost(postId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/likes")
  public ResponseEntity<PostLikePageResponse> getPostLikes(
      @RequestHeader(name = "memberId") Long memberId,
      @PageableDefault(
              page = 0,
              size = 8,
              sort = {"viewCount"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    PostLikePageResponse postLikePageResponse = postService.getPostLikes(memberId, pageable);
    return ResponseEntity.ok(postLikePageResponse);
  }
}
