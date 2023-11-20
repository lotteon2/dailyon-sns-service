package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository {

  Comment findById(Long id);

  Page<Comment> findAllByPostId(Long postId, Pageable pageable);

  Comment save(Comment comment);

  void deleteById(Long commentId);
}
