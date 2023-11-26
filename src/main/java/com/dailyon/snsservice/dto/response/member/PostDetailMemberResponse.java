package com.dailyon.snsservice.dto.response.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailMemberResponse {

  private Long id;
  private String nickname;
  private String profileImgUrl;
  private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean isFollowing;
}
