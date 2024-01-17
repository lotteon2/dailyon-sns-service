package com.dailyon.snsservice.service.post;

import com.dailyon.snsservice.dto.response.post.PostAdminPageResponse;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostAdminService {

  private final PostRepository postRepository;

  public PostAdminPageResponse getPostsForAdmin(Pageable pageable) {
    Page<Post> posts = postRepository.findAllByIdAscAndIsDeletedFalse(pageable);
    return PostAdminPageResponse.fromEntity(posts);
  }

  public void softBulkDeleteByIds(List<Long> ids) {
    postRepository.softBulkDeleteByIds(ids);
  }
}
