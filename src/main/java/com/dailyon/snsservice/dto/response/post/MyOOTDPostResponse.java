package com.dailyon.snsservice.dto.response.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyOOTDPostResponse {

  private Long id;
  private String thumbnailImgUrl;
  private Integer likeCount;
  private Integer viewCount;
  @JsonIgnore private Integer commentCount;
  private Boolean isLike;

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }
}
