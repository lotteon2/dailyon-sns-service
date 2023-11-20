package com.dailyon.snsservice.repository.post;

import com.dailyon.snsservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

  Post findByIdForUpdate(Long id);
  Page<Post> findAllWithIsLike(Long memberId, Pageable pageable);
  Post save(Post post);
  void softDeleteById(Long id);
  Page<Post> findAllWithPostLike(Long memberId, Pageable pageable);
}
