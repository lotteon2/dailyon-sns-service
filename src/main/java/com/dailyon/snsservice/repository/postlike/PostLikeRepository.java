package com.dailyon.snsservice.repository.postlike;

public interface PostLikeRepository {

  void togglePostLike(Long memberId, Long postId);
}
