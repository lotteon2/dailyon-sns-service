package com.dailyon.snsservice.entity.ids;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PostLikeId implements Serializable {

  private Long member;
  private Long post;
}
