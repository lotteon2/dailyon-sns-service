package com.dailyon.snsservice.repository.comment;

import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.dailyon.snsservice.entity.QComment.comment;
import static com.dailyon.snsservice.entity.QHashTag.hashTag;
import static com.dailyon.snsservice.entity.QMember.member;
import static com.dailyon.snsservice.entity.QPost.post;
import static com.dailyon.snsservice.entity.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

  private final CommentJpaRepository commentJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Comment> findAllByPostId(Long postId, Pageable pageable) {
    JPAQuery<Long> indexQuery =
        jpaQueryFactory
            .selectDistinct(comment.id)
            .from(comment)
            .leftJoin(comment.post, post)
            .where(post.id.eq(postId).and(comment.parent.isNull()))
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
    List<Long> indexes = indexQuery.fetch();
    if (indexes.isEmpty()) {
      return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    JPAQuery<Comment> resultQuery =
        jpaQueryFactory
            .selectDistinct(comment)
            .from(comment)
            .leftJoin(comment.children)
            .fetchJoin()
            .leftJoin(comment.member, member)
            .fetchJoin()
            .leftJoin(comment.post, post)
            .fetchJoin()
            .where(comment.id.in(indexes))
            .orderBy(getOrderCondition(pageable.getSort()).toArray(OrderSpecifier[]::new));

    List<Comment> result = resultQuery.fetch();

    JPAQuery<Long> countQuery =
        jpaQueryFactory
            .select(comment.id)
            .from(comment)
            .where(post.id.eq(postId).and(comment.parent.isNull()));

    long total = countQuery.fetchCount();
    return new PageImpl<>(result, pageable, total);
  }

  @Override
  public Comment save(Comment comment) {
    return commentJpaRepository.save(comment);
  }

  @Override
  public void softDeleteById(Long commentId, Long postId, Long memberId) {
    Comment comment =
        commentJpaRepository
            .findByIdAndPostIdAndMemberId(commentId, postId, memberId)
            .orElseThrow(CommentEntityNotFoundException::new);
    comment.setDeleted(true);
  }

  private List<OrderSpecifier> getOrderCondition(Sort sort) {
    List<OrderSpecifier> orders = new ArrayList<>();
    sort.stream()
        .forEach(
            order -> {
              Order direction = order.isAscending() ? Order.ASC : Order.DESC;
              String property = order.getProperty();
              PathBuilder<Comment> orderByExpression = new PathBuilder<>(Comment.class, "comment");
              orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
            });
    return orders;
  }
}
