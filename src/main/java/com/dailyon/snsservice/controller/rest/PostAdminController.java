package com.dailyon.snsservice.controller.rest;

import com.dailyon.snsservice.dto.response.post.PostAdminPageResponse;
import com.dailyon.snsservice.service.post.PostAdminService;
import com.dailyon.snsservice.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class PostAdminController {

  private final PostAdminService postAdminService;

  @GetMapping("/posts")
  public ResponseEntity<PostAdminPageResponse> getPosts(
      @PageableDefault(
              page = 0,
              size = 5,
              sort = {"id"},
              direction = Sort.Direction.ASC)
          Pageable pageable) {
    PostAdminPageResponse postAdminPageResponse = postAdminService.getPostsForAdmin(pageable);
    return ResponseEntity.ok(postAdminPageResponse);
  }

  @DeleteMapping("/posts")
  public ResponseEntity<Void> bulkDeletePosts(@RequestParam(name = "postIds") List<Long> postIds) {
    postAdminService.softBulkDeleteByIds(postIds);
    return ResponseEntity.ok().build();
  }
}
