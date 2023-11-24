package com.dailyon.snsservice.repository.follow;

import static com.dailyon.snsservice.entity.QFollow.follow;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {

  private final MemberJpaRepository memberJpaRepository;
  private final FollowJpaRepository followJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public void toggleFollow(Long followerId, Long followingId) {
    Follow findFollow =
        jpaQueryFactory
            .selectFrom(follow)
            .where(follow.follower.id.eq(followerId).and(follow.following.id.eq(followingId)))
            .fetchOne();

    if (findFollow == null) {
      Member findFollower =
          memberJpaRepository.findById(followerId).orElseThrow(MemberEntityNotFoundException::new);
      Member findFollowing =
          memberJpaRepository.findById(followingId).orElseThrow(MemberEntityNotFoundException::new);
      followJpaRepository.save(Follow.createFollow(findFollower, findFollowing));
    } else {
      followJpaRepository.delete(findFollow);
    }
  }

  @Override
  public Page<Follow> findFollowingsByMemberId(Long memberId, Pageable pageable) {
    return followJpaRepository.findFollowingsByMemberId(memberId, pageable);
  }

  @Override
  public Page<FollowerResponse> findFollowersByMemberId(Long memberId, Pageable pageable) {
    return followJpaRepository.findFollowersByMemberId(memberId, pageable);
  }
}
