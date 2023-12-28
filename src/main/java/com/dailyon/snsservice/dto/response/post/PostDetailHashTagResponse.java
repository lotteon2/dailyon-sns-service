package com.dailyon.snsservice.dto.response.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PostDetailHashTagResponse {

  private Long id;
  private String name;

  @QueryProjection
  public PostDetailHashTagResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
