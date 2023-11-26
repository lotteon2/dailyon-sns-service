package com.dailyon.snsservice.dto.response.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class PostDetailHashTagResponse {

  private Long id;
  private String name;

  public PostDetailHashTagResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
