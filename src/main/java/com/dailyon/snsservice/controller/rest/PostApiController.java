package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.dto.request.post.UpdatePostRequest;
import com.dailyon.snsservice.dto.response.post.*;
import com.dailyon.snsservice.dto.response.postlike.PostLikePageResponse;
import com.dailyon.snsservice.exception.HashTagDuplicatedException;
import com.dailyon.snsservice.service.post.PostService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
    origins = {"http://localhost:5173", "http://127.0.0.1:5173"},
    allowCredentials = "true",
    allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;

  @PostMapping("/posts")
  public ResponseEntity<CreatePostResponse> createPost(
      @RequestHeader(name = "memberId") Long memberId,
      @Valid @RequestBody CreatePostRequest createPostRequest) {
    CreatePostResponse createPostResponse = postService.createPost(memberId, createPostRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(createPostResponse);
  }

  @GetMapping("/posts")
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

  @GetMapping("/posts/{postId}")
  public ResponseEntity<Map<String, PostDetailResponse>> getPostDetail(
      @RequestHeader(name = "memberId", required = false) Long memberId,
      @PathVariable("postId") Long postId) {
    PostDetailResponse postDetailResponse =
        postService.findDetailByIdWithIsFollowing(postId, memberId);
    return ResponseEntity.ok(Map.of("post", postDetailResponse));
  }

  @PutMapping("/posts/{postId}")
  public ResponseEntity<UpdatePostResponse> updatePost(
      @RequestHeader(name = "memberId") Long memberId,
      @PathVariable("postId") Long postId,
      @Valid @RequestBody UpdatePostRequest updatePostRequest) {
    UpdatePostResponse updatePostResponse;
    try {
      updatePostResponse = postService.updatePost(postId, memberId, updatePostRequest);
    } catch (DataIntegrityViolationException e) {
      throw new HashTagDuplicatedException();
    }
    return ResponseEntity.ok(updatePostResponse);
  }

  @DeleteMapping("/posts/{postId}")
  public ResponseEntity<Void> deletePost(
      @RequestHeader(name = "memberId") Long memberId, @PathVariable("postId") Long postId) {
    postService.softDeletePost(postId, memberId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/posts/likes")
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

  @GetMapping("/my-posts")
  public ResponseEntity<OOTDPostPageResponse> getMyOOTDPosts(
      @RequestHeader(name = "memberId") Long memberId,
      @PageableDefault(
              page = 0,
              size = 6,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    OOTDPostPageResponse OOTDPostPageResponse = postService.getMyOOTDPosts(memberId, pageable);
    return ResponseEntity.ok(OOTDPostPageResponse);
  }

  @GetMapping("/member-posts")
  public ResponseEntity<OOTDPostPageResponse> getMemberOOTDPosts(
      @RequestHeader(name = "memberId") Long memberId,
      @RequestParam(name = "postMemberId") Long postMemberId,
      @PageableDefault(
              page = 0,
              size = 6,
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          Pageable pageable) {
    OOTDPostPageResponse OOTDPostPageResponse = postService.getMyOOTDPosts(postMemberId, pageable);
    return ResponseEntity.ok(OOTDPostPageResponse);
  }

  @GetMapping("/top4-posts")
  public ResponseEntity<Map<String, List<Top4OOTDResponse>>> getTop4OOTDPosts(
      @RequestParam(name = "productId") Long productId) {
    List<Top4OOTDResponse> top4OOTDResponses = postService.getTop4OOTDPosts(productId);
    return ResponseEntity.ok(Map.of("posts", top4OOTDResponses));
  }

  @PutMapping("/posts/{postId}/view-count")
  public ResponseEntity<Void> addViewCount(
      @PathVariable("postId") Long postId, @RequestParam(name = "count") Integer count) {
    postService.addViewCount(postId, count);
    return ResponseEntity.ok().build();
  }
}
