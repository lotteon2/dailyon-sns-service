package com.dailyon.snsservice.dto.response.post;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

  private Long id;
  private String nickname;
  private String profileImgUrl;
}
