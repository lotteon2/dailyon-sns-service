package com.dailyon.snsservice.service.post;

import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.dailyon.snsservice.repository.post.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {

  private final PostJpaRepository postJpaRepository;

  public Post read(Long postId) {
    return postJpaRepository
        .findByIdAndIsDeletedFalse(postId)
        .orElseThrow(PostEntityNotFoundException::new);
  }
}
