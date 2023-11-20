package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;

import java.util.Optional;

public interface CommentRepository {

  Comment findById(Long id);

  Comment save(Comment comment);

  void deleteById(Long commentId);
}
