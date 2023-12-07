package com.dailyon.snsservice.repository.follow;

import static com.dailyon.snsservice.entity.QFollow.follow;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.entity.ids.FollowId;
import com.dailyon.snsservice.service.member.MemberReader;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {

  private final MemberReader memberReader;
  private final FollowJpaRepository followJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public void toggleFollow(Long followerId, List<Long> followingIds) {
    Member findFollower = memberReader.read(followerId);
    List<Follow> findFollows =
        jpaQueryFactory
            .selectFrom(follow)
            .where(follow.follower.id.eq(followerId).and(follow.following.id.in(followingIds)))
            .fetch();

    List<Member> findFollowings = memberReader.readAll(followingIds);
    List<FollowId> haveToDeleteFollowIds = new ArrayList<>();
    List<Follow> haveToAddFollows = new ArrayList<>();
    findFollowings.forEach(
        findFollowing -> {
          // 언팔로우
          if (findFollows.stream()
              .anyMatch(
                  findFollow -> findFollow.getFollowing().getId().equals(findFollowing.getId()))) {
            haveToDeleteFollowIds.add(new FollowId(followerId, findFollowing.getId()));
            findFollower.decreaseFollowingCount();
            findFollowing.decreaseFollowerCount();
          }
          // 팔로우
          else {
            haveToAddFollows.add(Follow.createFollow(findFollower, findFollowing));
            findFollower.increaseFollowingCount();
            findFollowing.increaseFollowerCount();
          }
        });

    followJpaRepository.deleteAllByIdInBatch(haveToDeleteFollowIds);
    followJpaRepository.saveAll(haveToAddFollows);
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
