package com.dailyon.snsservice.dto.response.post;

import java.util.List;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPageResponse {

  private Boolean hasNext;
  private List<PostResponse> posts;

  public static PostPageResponse fromDto(Page<PostResponse> postResponses) {
    return PostPageResponse.builder()
        .hasNext(postResponses.hasNext())
        .posts(postResponses.getContent())
        .build();
  }
}
