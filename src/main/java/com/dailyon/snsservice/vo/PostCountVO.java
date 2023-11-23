package com.dailyon.snsservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCountVO implements Serializable {

  private Integer viewCount;
  private Integer likeCount;
  private Integer commentCount;
}
