package com.dailyon.snsservice.dto.response.member;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OOTDMemberProfileResponse {

  private Long id;
  private String nickname;
  private String profileImgUrl;
  private Integer followingCount;
  private Integer followerCount;
  private Boolean isFollowing;
}
