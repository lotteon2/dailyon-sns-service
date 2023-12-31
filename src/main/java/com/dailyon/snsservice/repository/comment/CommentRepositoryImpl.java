package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

  private final CommentJpaRepository commentJpaRepository;

  @Override
  public Page<Comment> findAllByPostId(Long postId, Pageable pageable) {
    return commentJpaRepository.findAllByPostId(postId, pageable);
  }

  @Override
  public Comment save(Comment comment) {
    return commentJpaRepository.save(comment);
  }

  @Override
  public void softDeleteById(Long commentId, Long postId, Long memberId) {
    Comment comment =
        commentJpaRepository
            .findByIdAndPostIdAndMemberId(commentId, postId, memberId)
            .orElseThrow(CommentEntityNotFoundException::new);
    comment.setDeleted(true);
  }
}
