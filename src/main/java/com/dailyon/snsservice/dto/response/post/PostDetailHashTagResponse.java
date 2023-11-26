package com.dailyon.snsservice.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailHashTagResponse {

  private Long id;
  private String name;
}
