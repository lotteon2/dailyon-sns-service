package com.dailyon.snsservice.entity.ids;

import com.dailyon.snsservice.entity.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class HashTagId implements Serializable {

  private String name;
  private Post post;
}
