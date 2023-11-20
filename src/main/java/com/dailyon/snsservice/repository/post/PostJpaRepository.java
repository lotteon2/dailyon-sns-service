package com.dailyon.snsservice.repository.post;

import com.dailyon.snsservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {}
