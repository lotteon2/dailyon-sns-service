package com.dailyon.snsservice.repository.follow;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.entity.Follow;
import com.dailyon.snsservice.entity.ids.FollowId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowJpaRepository extends JpaRepository<Follow, FollowId> {

  @Query(
      value =
          "select f from Follow f "
              + "join fetch f.following following "
              + "where f.follower.id = :memberId",
      countQuery = "select count(f) from Follow f where f.follower.id = :memberId")
  Page<Follow> findFollowingsByMemberId(Long memberId, Pageable pageable);

  @Query(
      value =
          "select new com.dailyon.snsservice.dto.response.follow"
              + ".FollowerResponse(follower.id, follower.nickname, follower.profileImgUrl, "
              + "exists ("
              + "select f2 from Follow f2 "
              + "left join f2.following following "
              + "where following.id = follower.id and f2.follower.id = :memberId)"
              + ") "
              + "from Follow f "
              + "inner join f.follower follower "
              + "where f.following.id = :memberId",
      countQuery = "select count(f) from Follow f where f.following.id = :memberId")
  Page<FollowerResponse> findFollowersByMemberId(Long memberId, Pageable pageable);
}
