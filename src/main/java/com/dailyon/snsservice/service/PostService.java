package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.response.post.PostPageResponse;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  public PostPageResponse getPosts(Long memberId, Pageable pageable) {
    Page<Post> posts = postRepository.findAllWithIsLike(memberId, pageable);
    return PostPageResponse.fromEntity(memberId, posts);
  }
}
