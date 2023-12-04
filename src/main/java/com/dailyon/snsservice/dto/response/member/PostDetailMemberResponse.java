package com.dailyon.snsservice.dto.response.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class PostDetailMemberResponse {

  private Long id;
  private String nickname;
  private String profileImgUrl;
  private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean isFollowing;

  @QueryProjection
  public PostDetailMemberResponse(Long id, String nickname, String profileImgUrl, String code, Boolean isFollowing) {
    this.id = id;
    this.nickname = nickname;
    this.profileImgUrl = profileImgUrl;
    this.code = code;
    this.isFollowing = isFollowing;
  }

  @QueryProjection
  public PostDetailMemberResponse(Long id, String nickname, String profileImgUrl, String code) {
    this.id = id;
    this.nickname = nickname;
    this.profileImgUrl = profileImgUrl;
    this.code = code;
  }
}
