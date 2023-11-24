package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepository {

  Page<Comment> findAllByPostId(Long postId, Pageable pageable);

  Comment save(Comment comment);

  void softDeleteById(Long commentId, Long postId, Long memberId);
}
