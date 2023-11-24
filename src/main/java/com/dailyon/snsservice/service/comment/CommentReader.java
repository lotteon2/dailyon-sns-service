package com.dailyon.snsservice.service.comment;

import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import com.dailyon.snsservice.repository.comment.CommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReader {

  private final CommentJpaRepository commentJpaRepository;

  public Comment read(Long commentId) {
    return commentJpaRepository
        .findById(commentId)
        .orElseThrow(CommentEntityNotFoundException::new);
  }
}
