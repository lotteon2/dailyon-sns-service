package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.dto.response.member.OOTDMemberProfileResponse;
import com.dailyon.snsservice.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

  @Query(
      "select new com.dailyon.snsservice.dto.response.member"
          + ".OOTDMemberProfileResponse(m.id, m.nickname, m.profileImgUrl, m.followingCount, m.followerCount, "
          + "exists ("
          + "select f from Follow f "
          + "where f.follower.id = :followerId and f.following.id = :memberId)"
          + ") "
          + "from Member m "
          + "where m.id = :memberId")
  Optional<OOTDMemberProfileResponse> findOOTDMemberProfile(Long memberId, Long followerId);
}
