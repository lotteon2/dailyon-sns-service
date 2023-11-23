package com.dailyon.snsservice.repository.postlike;

import static com.dailyon.snsservice.entity.QPostLike.*;

import com.dailyon.snsservice.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

  private final PostLikeJpaRepository postLikeJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public int togglePostLike(Member member, Post post) {
    PostLike findPostLike =
        jpaQueryFactory
            .selectFrom(postLike)
            .where(postLike.member.id.eq(member.getId()).and(postLike.post.id.eq(post.getId())))
            .fetchOne();

    if (!Objects.nonNull(findPostLike)) {
      postLikeJpaRepository.save(PostLike.createPostLike(member, post));
      return 1;
    } else {
      postLikeJpaRepository.delete(findPostLike);
      return -1;
    }
  }
}
