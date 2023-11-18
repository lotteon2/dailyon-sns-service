package com.dailyon.snsservice.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

  private Long id;
  private String thumbnailImgUrl;
  private Integer likeCount;
  private Integer viewCount;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private Boolean isLike;
}
