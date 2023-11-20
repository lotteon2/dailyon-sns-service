package com.dailyon.snsservice.dto.response.post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLikeResponse {

  private Long id;
  private String thumbnailImgUrl;
}
