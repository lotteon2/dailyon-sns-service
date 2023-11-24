package com.dailyon.snsservice.repository.follow;

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
      countQuery = "select count(f) from Follow f")
  Page<Follow> findFollowingsByMemberId(Long memberId, Pageable pageable);
}
