package com.dailyon.snsservice.service.postlike;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.repository.postlike.PostLikeRepository;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.post.PostReader;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
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
  public void togglePostLike(Long memberId, List<Long> postIds) {
    Member member = memberReader.read(memberId);
    List<Post> posts = postReader.readAll(postIds);
    Map<Long, Integer> countMap = postLikeRepository.togglePostLike(member, posts);
    try {
      for (Post post : posts) {
        PostCountVO postCountVO =
            new PostCountVO(post.getViewCount(), post.getLikeCount(), post.getCommentCount());
        // cache hit 시 cache에 있는 likeCount 를 반환, 아니라면 postCountVO 반환
        PostCountVO cachedPostCountVO =
            postCountRedisRepository.findOrPutPostCountVO(
                String.valueOf(post.getId()), postCountVO);
        cachedPostCountVO.addLikeCount(countMap.get(post.getId()));
        // update like count to cache
        postCountRedisRepository.modifyPostCountVOAboutLikeCount(
                String.valueOf(post.getId()), cachedPostCountVO);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
