package com.dailyon.snsservice.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {

  private Long id;
  private String thumbnailImgUrl;
  private Integer likeCount;
  private Integer viewCount;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private Boolean isLike;
}
