package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.dto.request.post.UpdatePostRequest;
import com.dailyon.snsservice.dto.response.post.CreatePostResponse;
import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.dto.response.post.UpdatePostResponse;
import com.dailyon.snsservice.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public CreatePostResponse createPost(
      @RequestHeader(name = "memberId") Long memberId,
      @Valid @RequestBody CreatePostRequest createPostRequest) {
    return postService.createPost(memberId, createPostRequest);
  }

  @GetMapping
  public PostPageResponse getPosts(
      @RequestHeader(name = "memberId", required = false) Long memberId,
      @PageableDefault(
              page = 0,
              size = 8,
              sort = {"viewCount"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    return postService.getPosts(memberId, pageable);
  }

  @PutMapping("/{postId}")
  public UpdatePostResponse updatePost(
          @RequestHeader(name = "memberId") Long memberId,
          @PathVariable("postId") Long postId,
          @Valid @RequestBody UpdatePostRequest updatePostRequest) {
    return postService.updatePost(postId, updatePostRequest);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(
      @RequestHeader(name = "memberId", required = false) Long memberId,
      @PathVariable("postId") Long postId) {
    postService.softDeletePost(postId);
  }
}
