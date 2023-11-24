package com.dailyon.snsservice.service.postlike;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.repository.postlike.PostLikeRepository;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.post.PostReader;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeService {

  private final MemberReader memberReader;
  private final PostReader postReader;
  private final PostLikeRepository postLikeRepository;
  private final PostCountRedisRepository postCountRedisRepository;

  @Transactional
  public void togglePostLike(Long memberId, Long postId) {
    Member member = memberReader.read(memberId);
    Post post = postReader.read(postId);
    int todoCalcLikeCount = postLikeRepository.togglePostLike(member, post);
    try {
      PostCountVO postCountVO =
          new PostCountVO(
              post.getViewCount(), post.getLikeCount() + todoCalcLikeCount, post.getCommentCount());

      // update like count to cache
      postCountRedisRepository.modifyPostCountVOAboutLikeCount(String.valueOf(postId), postCountVO);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
