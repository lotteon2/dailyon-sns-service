package com.dailyon.snsservice.repository.post;

import static com.dailyon.snsservice.entity.QFollow.follow;
import static com.dailyon.snsservice.entity.QHashTag.hashTag;
import static com.dailyon.snsservice.entity.QMember.member;
import static com.dailyon.snsservice.entity.QPost.post;
import static com.dailyon.snsservice.entity.QPostImage.postImage;
import static com.dailyon.snsservice.entity.QPostImageProductDetail.postImageProductDetail;
import static com.dailyon.snsservice.entity.QPostLike.postLike;

import com.dailyon.snsservice.dto.response.member.PostDetailMemberResponse;
import com.dailyon.snsservice.dto.response.post.*;
import com.dailyon.snsservice.dto.response.postimageproductdetail.PostImageProductDetailResponse;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
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
                      new CaseBuilder()
                          .when(postLike.member.id.eq(memberId))
                          .then(true)
                          .otherwise(false)))
              .from(post)
              .leftJoin(post.postLikes, postLike);
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

    return new PageImpl<>(query.fetch(), pageable, getTotalPageCount());
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
  public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
    return postJpaRepository.findAllByMemberId(memberId, pageable);
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
  public PostDetailResponse findDetailByIdWithIsFollowing(Long id, Long memberId) {
    JPAQuery<PostDetailResponse> query;

    QList hashTags =
        Projections.list(
            Projections.constructor(PostDetailHashTagResponse.class, hashTag.id, hashTag.name));

    QList postImageProductDetails =
        Projections.list(
            Projections.constructor(
                PostImageProductDetailResponse.class,
                postImageProductDetail.id,
                postImageProductDetail.productId,
                postImageProductDetail.productSize,
                postImageProductDetail.leftGapPercent,
                postImageProductDetail.topGapPercent));

    if (Objects.nonNull(memberId)) {
      BooleanExpression isFollowingExpression =
          new CaseBuilder().when(follow.follower.id.eq(memberId)).then(true).otherwise(false);
      query =
          jpaQueryFactory
              .select(
                  Projections.constructor(
                      PostDetailResponse.class,
                      post.id,
                      post.title,
                      post.description,
                      post.stature,
                      post.weight,
                      postImage.imgUrl,
                      post.createdAt,
                      Projections.constructor(
                          PostDetailMemberResponse.class,
                          member.id,
                          member.nickname,
                          member.profileImgUrl,
                          member.code,
                          isFollowingExpression),
                      hashTags,
                      postImageProductDetails))
              .from(post)
              .leftJoin(post.member, member)
              .leftJoin(member.following, follow);
    } else {
      query =
          jpaQueryFactory
              .select(
                  Projections.constructor(
                      PostDetailResponse.class,
                      post.id,
                      post.title,
                      post.description,
                      post.stature,
                      post.weight,
                      postImage.imgUrl,
                      post.createdAt,
                      Projections.fields(
                          PostDetailMemberResponse.class,
                          member.id,
                          member.nickname,
                          member.profileImgUrl,
                          member.code),
                      hashTags,
                      postImageProductDetails))
              .from(post)
              .leftJoin(post.member, member);
    }

    return query
        .leftJoin(post.postImage, postImage)
        .leftJoin(postImage.postImageProductDetails, postImageProductDetail)
        .leftJoin(post.hashTags, hashTag)
        .where(post.id.eq(id), post.isDeleted.eq(false))
        .fetchOne();
  }

  private Long getTotalPageCount() {
    return jpaQueryFactory.select(post.count()).from(post).fetchOne();
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
