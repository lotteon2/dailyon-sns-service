package com.dailyon.snsservice.service.postlike;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.cache.PostCountRedisRepository;
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
      // cache hit: 기존의 캐시에 들어있는 count 반환
      // cache miss: count + 1 해서 캐시에 넣은 후 반환
      PostCountVO postCountVO =
          postCountRedisRepository.findOrPutPostCountVO(
              String.valueOf(postId),
              new PostCountVO(
                  post.getViewCount(),
                  post.getLikeCount() + todoCalcLikeCount,
                  post.getCommentCount()));
      // 이미 캐시에 존재하는 값이라면 업데이트
      if (postCountVO.getLikeCount().equals(post.getLikeCount() + todoCalcLikeCount)) {
        postCountVO.updateLikeCount(todoCalcLikeCount);
        postCountRedisRepository.modifyPostCountVOAboutLikeCount(String.valueOf(postId), postCountVO);
      }

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
