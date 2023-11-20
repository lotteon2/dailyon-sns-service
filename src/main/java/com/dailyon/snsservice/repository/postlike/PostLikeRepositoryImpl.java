package com.dailyon.snsservice.repository.postlike;

import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import com.dailyon.snsservice.repository.post.PostJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dailyon.snsservice.entity.QMember.member;
import static com.dailyon.snsservice.entity.QPost.post;
import static com.dailyon.snsservice.entity.QPostLike.*;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

  private final PostLikeJpaRepository postLikeJpaRepository;
  private final MemberJpaRepository memberJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public void togglePostLike(Long memberId, Long postId) {
    PostLike findPostLike =
        jpaQueryFactory
            .selectFrom(postLike)
            .innerJoin(postLike.member, member)
            .fetchJoin()
            .innerJoin(postLike.post, post)
            .fetchJoin()
            .where(postLike.member.id.eq(memberId).and(postLike.post.id.eq(postId)))
            .fetchOne();

    if (findPostLike == null) {
      Member findMember =
          memberJpaRepository.findById(memberId).orElseThrow(MemberEntityNotFoundException::new);
      Post findPost =
          postJpaRepository.findById(postId).orElseThrow(PostEntityNotFoundException::new);
      postLikeJpaRepository.save(PostLike.createPostLike(findMember, findPost));
    } else {
      postLikeJpaRepository.delete(findPostLike);
    }
  }
}
