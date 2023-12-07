package com.dailyon.snsservice.repository.postlike;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;

import java.util.List;
import java.util.Map;

public interface PostLikeRepository {

  Map<Long, Integer> togglePostLike(Member member, List<Post> posts);
}
