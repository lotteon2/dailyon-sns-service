package com.dailyon.snsservice.repository.follow;

import static com.dailyon.snsservice.entity.QFollow.follow;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.entity.ids.FollowId;
import com.dailyon.snsservice.service.member.MemberReader;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    JPAQuery<Long> indexQuery =
        jpaQueryFactory
            .selectDistinct(follow.follower.id)
            .from(follow)
            .leftJoin(follow.following)
            .where(follow.follower.id.eq(memberId))
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
    List<Long> indexes = indexQuery.fetch();
    if (indexes.isEmpty()) {
      return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    JPAQuery<Follow> resultQuery =
        jpaQueryFactory
            .selectDistinct(follow)
            .from(follow)
            .leftJoin(follow.following)
            .fetchJoin()
            .where(follow.follower.id.in(indexes))
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new));

    List<Follow> result = resultQuery.fetch();

    JPAQuery<Long> countQuery =
        jpaQueryFactory
            .select(follow.follower.id)
            .from(follow)
            .where(follow.follower.id.eq(memberId));

    long total = countQuery.fetchCount();
    return new PageImpl<>(result, pageable, total);
  }

  @Override
  public Page<FollowerResponse> findFollowersByMemberId(Long memberId, Pageable pageable) {
    JPAQuery<Long> indexQuery =
        jpaQueryFactory
            .select(follow.following.id)
            .from(follow)
            .innerJoin(follow.follower)
            .where(follow.following.id.eq(memberId))
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
    List<Long> indexes = indexQuery.fetch();
    if (indexes.isEmpty()) {
      return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    JPAQuery<FollowerResponse> resultQuery =
        jpaQueryFactory
            .select(
                Projections.constructor(
                    FollowerResponse.class,
                    follow.follower.id,
                    follow.follower.nickname,
                    follow.follower.profileImgUrl,
                    Expressions.booleanTemplate(
                        "exists (select f2 from Follow f2 "
                            + "left join f2.following following "
                            + "where following.id = {0} and f2.follower.id = {1})",
                            follow.follower.id, memberId)))
            .from(follow)
            .innerJoin(follow.follower)
            .where(follow.following.id.in(indexes))
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new));

    List<FollowerResponse> result = resultQuery.fetch();

    JPAQuery<Long> countQuery =
        jpaQueryFactory
            .select(follow.follower.id)
            .from(follow)
            .where(follow.following.id.eq(memberId));

    long total = countQuery.fetchCount();
    return new PageImpl<>(result, pageable, total);

    //    return followJpaRepository.findFollowersByMemberId(memberId, pageable);
  }

  private List<OrderSpecifier> getOrderCondition(Sort sort) {
    List<OrderSpecifier> orders = new ArrayList<>();
    sort.stream()
        .forEach(
            order -> {
              Order direction = order.isAscending() ? Order.ASC : Order.DESC;
              String property = order.getProperty();
              PathBuilder<Follow> orderByExpression = new PathBuilder<>(Follow.class, "follow");
              orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
            });
    return orders;
  }
}
