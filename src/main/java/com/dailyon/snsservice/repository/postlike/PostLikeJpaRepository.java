package com.dailyon.snsservice.repository.postlike;

import com.dailyon.snsservice.entity.PostLike;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, PostLikeId> {}
