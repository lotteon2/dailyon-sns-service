package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

  private final CommentJpaRepository commentJpaRepository;

  @Override
  public Comment findById(Long id) {
    return commentJpaRepository.findById(id).orElseThrow(CommentEntityNotFoundException::new);
  }

  @Override
  public Comment save(Comment comment) {
    return commentJpaRepository.save(comment);
  }
}
