package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c from Comment c " +
            "left join fetch c.children cd " +
            "join fetch c.member m " +
            "join fetch c.post p " +
            "where p.id = :postId and c.parent is null",
            countQuery = "select count(c) from Comment c where c.post.id = :postId and c.parent is null")
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
}
