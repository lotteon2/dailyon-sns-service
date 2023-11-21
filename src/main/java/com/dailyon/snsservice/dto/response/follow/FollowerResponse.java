package com.dailyon.snsservice.dto.response.follow;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {

  private Long id;
  private String nickname;
  private String profileImgUrl;
  private Boolean isFollowing;
}
