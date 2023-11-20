package com.dailyon.snsservice.dto.response.follow;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowingResponse {

  private Long id;
  private String nickname;
  private String profileImgUrl;
}
