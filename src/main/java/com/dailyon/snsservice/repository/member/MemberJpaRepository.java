package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  @Query(
      value =
          "select m from Member m "
              + "join fetch m.following f "
              + "where f.follower.id = :followerId",
      countQuery = "select count(m) from Member m")
  Page<Member> findFollowingsByFollowerId(Long followerId, Pageable pageable);
}
