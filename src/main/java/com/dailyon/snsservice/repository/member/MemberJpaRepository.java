package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
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

  @Query(
      value =
          "select new com.dailyon.snsservice.dto.response.follow.FollowerResponse(m.id, m.nickname, m.profileImgUrl, "
              + "exists (select m2 from Member m2 left join m2.followers f2 where f2.following.id = m.id and f2.follower.id = :followingId)) "
              + "from Member m "
              + "left join m.followers f "
              + "where f.following.id = :followingId",
      countQuery = "select count(m) from Member m")
  Page<FollowerResponse> findFollowersByFollowingId(Long followingId, Pageable pageable);
}
