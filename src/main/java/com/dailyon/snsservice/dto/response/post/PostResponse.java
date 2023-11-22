package com.dailyon.snsservice.dto.response.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  private Long id;
  private String thumbnailImgUrl;
  private Integer likeCount;
  private Integer viewCount;

  @JsonIgnore
  private Integer commentCount;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private Boolean isLike;

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }
}
