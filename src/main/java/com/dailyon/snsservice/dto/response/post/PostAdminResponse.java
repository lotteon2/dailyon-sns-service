package com.dailyon.snsservice.dto.response.post;

import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAdminResponse {

  private Long id;
  private String thumbnailImgUrl;
  private List<String> hashTagNames;
  private String title;
  private String description;
  private String memberNickname;
}
