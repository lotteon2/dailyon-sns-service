package com.dailyon.snsservice.dto.response.post;

import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.vo.Top4OOTDVO;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Top4OOTDResponse {

  private Long id;
  private String thumbnailImgUrl;

  public static Top4OOTDResponse fromEntity(Post post) {
      return Top4OOTDResponse.builder()
              .id(post.getId())
              .thumbnailImgUrl(post.getPostImage().getThumbnailImgUrl())
              .build();
  }

  public static Top4OOTDResponse fromTop4OOTDVO(Top4OOTDVO top4OOTDVO) {
    return Top4OOTDResponse.builder()
        .id(top4OOTDVO.getId())
        .thumbnailImgUrl(top4OOTDVO.getThumbnailImgUrl())
        .build();
  }
}
