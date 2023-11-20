package com.dailyon.snsservice.dto.response.postlike;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeResponse {

  private Long id;
  private String thumbnailImgUrl;
}
