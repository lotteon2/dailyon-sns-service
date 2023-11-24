package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

  @Query(
      value =
          "select c from Comment c "
              + "left join fetch c.children cd "
              + "join fetch c.member m "
              + "join fetch c.post p "
              + "where p.id = :postId and c.parent is null and c.isDeleted = false",
      countQuery =
          "select count(c) from Comment c "
              + "where c.post.id = :postId and c.parent is null and c.isDeleted = false")
  Page<Comment> findAllByPostId(Long postId, Pageable pageable);

  @Query(
      "select c from Comment c "
          + "left join fetch c.children cd "
          + "join fetch c.member m "
          + "join fetch c.post p "
          + "where c.id = :id and c.post.id = :postId and c.member.id = :memberId and c.isDeleted = false")
  Optional<Comment> findByIdAndPostIdAndMemberId(Long id, Long postId, Long memberId);

  Optional<Comment> findByIdAndIsDeletedFalse(Long id);
}
