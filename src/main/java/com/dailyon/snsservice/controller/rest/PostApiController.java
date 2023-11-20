package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;

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
}
