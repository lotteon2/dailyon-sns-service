package com.dailyon.snsservice.repository.post;

import static com.dailyon.snsservice.entity.QPost.post;
import static com.dailyon.snsservice.entity.QPostLike.postLike;

import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
  public Post findByIdForUpdate(Long id) {
    return postJpaRepository.findByIdForUpdate(id).orElseThrow(PostEntityNotFoundException::new);
  }

  @Override
  public Page<Post> findAllWithIsLike(Long memberId, Pageable pageable) {
    JPAQuery<Post> query =
        jpaQueryFactory
            .selectFrom(post)
            .innerJoin(post.postImage)
            .fetchJoin()
            .where(post.isDeleted.isFalse())
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

    if (Objects.nonNull(memberId)) {
      query
          .leftJoin(postLike)
          .on(postLike.post.id.eq(post.id).and(postLike.member.id.eq(memberId)));
    }

    return new PageImpl<>(query.fetch(), pageable, getTotalPageCount());
  }

  @Override
  public Post save(Post post) {
    return postJpaRepository.save(post);
  }

  @Override
  public void softDeleteById(Long id) {
    Post post = postJpaRepository.findById(id).orElseThrow(PostEntityNotFoundException::new);
    post.setDeleted(true);
  }

  @Override
  public Page<Post> findAllWithPostLike(Long memberId, Pageable pageable) {
    return postJpaRepository.findAllWithPostLikeByMemberId(memberId, pageable);
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
              PathBuilder orderByExpression = new PathBuilder(Post.class, "post");
              orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
            });
    return orders;
  }
}
