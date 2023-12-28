package com.dailyon.snsservice.dto.response.post;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostResponse {

  private Long id;
  private String thumbnailImgPreSignedUrl;
  private String imgPreSignedUrl;
}
