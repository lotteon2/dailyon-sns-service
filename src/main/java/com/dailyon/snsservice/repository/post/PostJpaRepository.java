package com.dailyon.snsservice.repository.post;

import com.dailyon.snsservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

  @Query(
      "select p from Post p "
          + "join fetch p.postImage pi "
          + "join fetch p.hashTags ht "
          + "join fetch pi.postImageProductDetails pipd "
          + "where p.id = :id")
  Optional<Post> findByIdForUpdate(Long id);

  @Query(value = "select p from Post p " +
          "join fetch p.postImage pi " +
          "join fetch p.postLikes pl " +
          "where p.member.id = :memberId",
          countQuery = "select count(p) from Post p where p.member.id = :memberId")
  Page<Post> findAllWithPostLikeByMemberId(Long memberId, Pageable pageable);

  @Query(value = "select p from Post p " +
          "join fetch p.postImage pi " +
          "where p.member.id = :memberId",
          countQuery = "select count(p) from Post p where p.member.id = :memberId")
  Page<Post> findAllByMemberId(Long memberId, Pageable pageable);
}
