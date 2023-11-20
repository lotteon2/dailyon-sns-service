package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {}
