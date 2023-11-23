package com.dailyon.snsservice.repository.postlike;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;

public interface PostLikeRepository {

  int togglePostLike(Member member, Post post);
}
