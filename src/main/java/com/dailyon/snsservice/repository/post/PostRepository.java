package com.dailyon.snsservice.repository.post;

import com.dailyon.snsservice.dto.response.post.PostResponse;
import com.dailyon.snsservice.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository {

  Post findByIdForUpdate(Long id);

  Page<PostResponse> findAllWithIsLike(Long memberId, Pageable pageable);

  Post save(Post post);

  void softDeleteById(Long id);

  Page<Post> findAllWithPostLike(Long memberId, Pageable pageable);

  Page<Post> findAllByMemberId(Long memberId, Pageable pageable);

  List<Post> findTop4ByOrderByLikeCountDesc(Long productId);

  int updateCountsById(Long id, Integer viewCount, Integer likeCount, Integer commentCount);
}
