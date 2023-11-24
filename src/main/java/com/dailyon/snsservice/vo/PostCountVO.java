package com.dailyon.snsservice.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PostCountVO implements Serializable {

  private Integer viewCount;
  private Integer likeCount;
  private Integer commentCount;

  public void updateLikeCount(Integer count) {
    this.likeCount += count;
  }

  public void addCommentCount() {
    this.commentCount += 1;
  }
}
