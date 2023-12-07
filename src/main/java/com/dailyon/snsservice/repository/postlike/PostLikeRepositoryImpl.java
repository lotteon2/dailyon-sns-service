package com.dailyon.snsservice.repository.postlike;

import static com.dailyon.snsservice.entity.QPostLike.*;

import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

  private final PostLikeJpaRepository postLikeJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Map<Long, Integer> togglePostLike(Member member, List<Post> posts) {
    List<PostLike> postLikes =
        jpaQueryFactory
            .selectFrom(postLike)
            .innerJoin(postLike.post)
            .fetchJoin()
            .where(postLike.member.eq(member).and(postLike.post.in(posts)))
            .fetch();

    Map<Long, Integer> countMap = new HashMap<>();
    List<PostLikeId> haveToDeletePostLikeIds = new ArrayList<>();
    List<PostLike> haveToAddPostLikes = new ArrayList<>();
    posts.forEach(
        (post) -> {
          if (postLikes.stream()
              .anyMatch((postLike) -> postLike.getPost().getId().equals(post.getId()))) {
            haveToDeletePostLikeIds.add(new PostLikeId(member.getId(), post.getId()));
            countMap.put(post.getId(), -1);
          } else {
            PostLike newPostLike = PostLike.createPostLike(member, post);
            haveToAddPostLikes.add(newPostLike);
            countMap.put(post.getId(), 1);
          }
        });

    postLikeJpaRepository.deleteAllByIdInBatch(haveToDeletePostLikeIds);
    postLikeJpaRepository.saveAll(haveToAddPostLikes);

    return countMap;
  }
}
