package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.follow.FollowerResponse;
import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  @Query(
      value =
          "select new com.dailyon.snsservice.dto.response.follow.FollowerResponse(m.id, m.nickname, m.profileImgUrl, "
              + "exists (select m2 from Member m2 left join m2.followers f2 where f2.following.id = m.id and f2.follower.id = :followingId)) "
              + "from Member m "
              + "left join m.followers f "
              + "where f.following.id = :followingId",
      countQuery = "select count(m) from Member m")
  Page<FollowerResponse> findFollowersByFollowingId(Long followingId, Pageable pageable);

  @Query(
      "select new com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse(m.id, m.nickname, m.profileImgUrl, m.followingCount, m.followerCount, "
          + "exists (select f from Follow f where f.follower.id = :followerId and f.following.id = :memberId)) "
          + "from Member m where m.id = :memberId")
  Optional<OOTDMemberProfileResponse> findOOTDMemberProfile(Long memberId, Long followerId);
}
