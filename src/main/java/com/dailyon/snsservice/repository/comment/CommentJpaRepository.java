package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

  @Query(
      "select c from Comment c "
          + "left join fetch c.children cd "
          + "join fetch c.member m "
          + "join fetch c.post p "
          + "where c.id = :id and c.post.id = :postId and c.member.id = :memberId")
  Optional<Comment> findByIdAndPostIdAndMemberId(Long id, Long postId, Long memberId);
}
