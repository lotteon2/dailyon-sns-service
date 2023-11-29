package com.dailyon.snsservice.repository.post;

import com.dailyon.snsservice.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

  Optional<Post> findByIdAndIsDeletedFalse(Long id);

  @Query(
      "select p from Post p "
          + "join fetch p.postImage pi "
          + "join fetch p.hashTags ht "
          + "join fetch pi.postImageProductDetails pipd "
          + "where p.id = :id and p.member.id = :memberId and p.isDeleted = false")
  Optional<Post> findByIdAndMemberIdForUpdate(Long id, Long memberId);

  @Query(
      value =
          "select p from Post p "
              + "join fetch p.postImage pi "
              + "join fetch p.postLikes pl "
              + "where pl.member.id in :memberId and p.isDeleted = false",
      countQuery =
          "select count(p) from Post p join p.postLikes pl where p.member.id = :memberId and p.isDeleted = false")
  Page<Post> findAllWithPostLikeByMemberIdIn(Long memberId, Pageable pageable);

  @Query(
      "select p from Post p "
          + "join fetch p.postImage pi "
          + "join fetch pi.postImageProductDetails pipd "
          + "where pipd.productId = :productId and p.isDeleted = false")
  List<Post> findTop4ByOrderByLikeCountDesc(Long productId, Pageable pageable);

  // 트랜잭션이 COMMIT 될 때 영속성 컨텍스트를 flush
  // JPQL 연산이 끝난 후 영속성 컨텍스트를 비워줌
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update Post p "
          + "set p.viewCount = :viewCount, p.likeCount = :likeCount, p.commentCount = :commentCount "
          + "where p.id = :id and p.isDeleted = false")
  int updateCountsById(
      @Param("id") Long id,
      @Param("viewCount") Integer viewCount,
      @Param("likeCount") Integer likeCount,
      @Param("commentCount") Integer commentCount);

  @Query("select p from Post p where p.id = :id and p.member.id = :memberId")
  Optional<Post> findByIdAndMemberId(Long id, Long memberId);
}
