package com.dailyon.snsservice.repository.post;

import static com.dailyon.snsservice.entity.QHashTag.hashTag;
import static com.dailyon.snsservice.entity.QMember.member;
import static com.dailyon.snsservice.entity.QPost.post;
import static com.dailyon.snsservice.entity.QPostImage.postImage;
import static com.dailyon.snsservice.entity.QPostImageProductDetail.postImageProductDetail;
import static com.querydsl.core.group.GroupBy.*;

import com.dailyon.snsservice.dto.response.member.PostDetailMemberResponse;
import com.dailyon.snsservice.dto.response.member.QPostDetailMemberResponse;
import com.dailyon.snsservice.dto.response.post.*;
import com.dailyon.snsservice.dto.response.postimageproductdetail.PostImageProductDetailResponse;
import com.dailyon.snsservice.dto.response.postimageproductdetail.QPostImageProductDetailResponse;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.entity.QFollow;
import com.dailyon.snsservice.entity.QPostLike;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

  private final PostJpaRepository postJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Post findByIdAndIsDeletedFalse(Long id) {
    return postJpaRepository
        .findByIdAndIsDeletedFalse(id)
        .orElseThrow(PostEntityNotFoundException::new);
  }

  @Override
  public Post findByIdAndMemberIdForUpdate(Long id, Long memberId) {
    return postJpaRepository
        .findByIdAndMemberIdForUpdate(id, memberId)
        .orElseThrow(PostEntityNotFoundException::new);
  }

  @Override
  public Page<PostResponse> findAllWithIsLike(Long memberId, Pageable pageable) {
    JPAQuery<PostResponse> query;
    if (Objects.nonNull(memberId)) {
      QPostLike postLikeSubQuery = new QPostLike("postLikeSubQuery");

      BooleanExpression hasLikedCondition =
          JPAExpressions.select(postLikeSubQuery)
              .from(postLikeSubQuery)
              .where(postLikeSubQuery.post.id.eq(post.id), postLikeSubQuery.member.id.eq(memberId))
              .exists();

      query =
          jpaQueryFactory
              .select(
                  Projections.constructor(
                      PostResponse.class,
                      post.id,
                      post.postImage.thumbnailImgUrl,
                      post.likeCount,
                      post.viewCount,
                      post.commentCount,
                      hasLikedCondition))
              .from(post);

    } else {
      query =
          jpaQueryFactory
              .select(
                  Projections.fields(
                      PostResponse.class,
                      post.id,
                      post.postImage.thumbnailImgUrl,
                      post.likeCount,
                      post.viewCount,
                      post.commentCount))
              .from(post);
    }

    query
        .innerJoin(post.postImage)
        .where(post.isDeleted.isFalse())
        .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    return new PageImpl<>(query.fetch(), pageable, getPostTotalPageCount());
  }

  @Override
  public Post save(Post post) {
    return postJpaRepository.save(post);
  }

  @Override
  public void softDeleteById(Long id, Long memberId) {
    Post post =
        postJpaRepository
            .findByIdAndMemberId(id, memberId)
            .orElseThrow(PostEntityNotFoundException::new);
    post.setDeleted(true);
  }

  @Override
  public Page<Post> findAllWithPostLikeByMemberIdIn(Long memberId, Pageable pageable) {
    return postJpaRepository.findAllWithPostLikeByMemberIdIn(memberId, pageable);
  }

  @Override
  public Page<OOTDPostResponse> findMyPostsByMemberId(Long memberId, Pageable pageable) {
    QPostLike postLikeSubQuery = new QPostLike("postLikeSubQuery");

    BooleanExpression hasLikedCondition =
        JPAExpressions.select(postLikeSubQuery)
            .from(postLikeSubQuery)
            .where(postLikeSubQuery.post.id.eq(post.id), postLikeSubQuery.member.id.eq(memberId))
            .exists();

    JPAQuery<OOTDPostResponse> query =
        jpaQueryFactory
            .select(
                Projections.constructor(
                    OOTDPostResponse.class,
                    post.id,
                    post.postImage.thumbnailImgUrl,
                    post.likeCount,
                    post.viewCount,
                    post.commentCount,
                    hasLikedCondition))
            .from(post)
            .innerJoin(post.postImage)
            .where(post.member.id.eq(memberId), post.isDeleted.isFalse())
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

    Long totalElements = getMyPageTotalPageCount(pageable, memberId);

    return new PageImpl<>(query.fetch(), pageable, totalElements);
  }

  @Override
  public Page<OOTDPostResponse> findMemberPostsByMemberId(Long postMemberId, Long memberId, Pageable pageable) {
    QPostLike postLikeSubQuery = new QPostLike("postLikeSubQuery");

    BooleanExpression hasLikedCondition =
            JPAExpressions.select(postLikeSubQuery)
                    .from(postLikeSubQuery)
                    .where(postLikeSubQuery.post.id.eq(post.id), postLikeSubQuery.member.id.eq(memberId))
                    .exists();

    JPAQuery<OOTDPostResponse> query =
            jpaQueryFactory
                    .select(
                            Projections.constructor(
                                    OOTDPostResponse.class,
                                    post.id,
                                    post.postImage.thumbnailImgUrl,
                                    post.likeCount,
                                    post.viewCount,
                                    post.commentCount,
                                    hasLikedCondition))
                    .from(post)
                    .innerJoin(post.postImage)
                    .where(post.member.id.eq(postMemberId), post.isDeleted.isFalse())
                    .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

    Long totalElements = getMyPageTotalPageCount(pageable, postMemberId);

    return new PageImpl<>(query.fetch(), pageable, totalElements);
  }

  @Override
  public List<Post> findTop4ByOrderByLikeCountDesc(Long productId) {
    PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "likeCount"));
    return postJpaRepository.findTop4ByOrderByLikeCountDesc(productId, pageRequest);
  }

  @Override
  public int updateCountsById(Long id, Integer viewCount, Integer likeCount, Integer commentCount) {
    return postJpaRepository.updateCountsById(id, viewCount, likeCount, commentCount);
  }

  @Override
  public PostDetailResponse findDetailByIdWithIsFollowingAndIsLike(Long id, Long memberId) {
    JPAQuery<?> query =
        jpaQueryFactory
            .from(post)
            .innerJoin(post.member, member)
            .innerJoin(post.postImage, postImage)
            .innerJoin(post.hashTags, hashTag)
            .leftJoin(postImage.postImageProductDetails, postImageProductDetail)
            .where(post.id.eq(id), post.isDeleted.eq(false));

    if (Objects.nonNull(memberId)) {
      QPostLike postLikeSubQuery = new QPostLike("postLikeSubQuery");

      BooleanExpression hasLikedCondition =
          JPAExpressions.select(postLikeSubQuery)
              .from(postLikeSubQuery)
              .where(postLikeSubQuery.post.id.eq(post.id), postLikeSubQuery.member.id.eq(memberId))
              .exists();

      QFollow followSubQuery = new QFollow("followSubQuery");

      BooleanExpression isFollowingExpression =
          JPAExpressions.select(followSubQuery)
              .from(followSubQuery)
              .where(
                  followSubQuery.following.id.eq(member.id),
                  followSubQuery.follower.id.eq(memberId))
              .exists();

      return query
          .transform(
              groupBy(post.id)
                  .as(
                      new QPostDetailResponse(
                          post.id,
                          post.title,
                          post.description,
                          post.stature,
                          post.weight,
                          postImage.imgUrl,
                          post.viewCount,
                          post.likeCount,
                          post.commentCount,
                          hasLikedCondition,
                          post.createdAt,
                          new QPostDetailMemberResponse(
                              member.id,
                              member.nickname,
                              member.profileImgUrl,
                              member.code,
                              isFollowingExpression),
                          set(new QPostDetailHashTagResponse(hashTag.id, hashTag.name)),
                          set(
                              new QPostImageProductDetailResponse(
                                  postImageProductDetail.id,
                                  postImageProductDetail.productId,
                                  postImageProductDetail.productSize,
                                  postImageProductDetail.leftGapPercent,
                                  postImageProductDetail.topGapPercent)))))
          .get(id);
    } else {
      return query
          .transform(
              groupBy(post.id)
                  .as(
                      new QPostDetailResponse(
                          post.id,
                          post.title,
                          post.description,
                          post.stature,
                          post.weight,
                          postImage.imgUrl,
                          post.viewCount,
                          post.likeCount,
                          post.commentCount,
                          post.createdAt,
                          new QPostDetailMemberResponse(
                              member.id, member.nickname, member.profileImgUrl, member.code),
                          set(new QPostDetailHashTagResponse(hashTag.id, hashTag.name)),
                          set(
                              new QPostImageProductDetailResponse(
                                  postImageProductDetail.id,
                                  postImageProductDetail.productId,
                                  postImageProductDetail.productSize,
                                  postImageProductDetail.leftGapPercent,
                                  postImageProductDetail.topGapPercent)))))
          .get(id);
    }
  }

  private Long getMyPageTotalPageCount(Pageable pageable, Long memberId) {
    return jpaQueryFactory
        .select(post.count())
        .from(post)
        .where(post.member.id.eq(memberId), post.isDeleted.isFalse())
        .fetchOne();
  }

  private Long getPostTotalPageCount() {
    return jpaQueryFactory
        .select(post.count())
        .from(post)
        .where(post.isDeleted.isFalse())
        .fetchOne();
  }

  private List<OrderSpecifier> getOrderCondition(Sort sort) {
    List<OrderSpecifier> orders = new ArrayList<>();
    sort.stream()
        .forEach(
            order -> {
              Order direction = order.isAscending() ? Order.ASC : Order.DESC;
              String property = order.getProperty();
              PathBuilder<Post> orderByExpression = new PathBuilder<>(Post.class, "post");
              orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
            });
    return orders;
  }
}
