package com.dailyon.snsservice.repository.post;

import static com.dailyon.snsservice.entity.QPost.post;
import static com.dailyon.snsservice.entity.QPostLike.postLike;

import com.dailyon.snsservice.entity.Post;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

  private final PostJpaRepository postJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

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
          .on(postLike.post.id.eq(post.id).and(postLike.member.id.eq(memberId)))
          .where(postLike.member.id.eq(memberId));
    }

    return new PageImpl<>(query.fetch(), pageable, getTotalPageCount());
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
