package com.dailyon.snsservice.entity.ids;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class PostLikeId implements Serializable {

  private Member member;
  private Post post;
}
