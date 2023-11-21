package com.dailyon.snsservice.dto.response.post;

import com.dailyon.snsservice.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OOTDPostPageResponse {

  private Boolean hasNext;
  private List<OOTDPostResponse> posts;

  public static OOTDPostPageResponse fromEntity(Page<Post> posts) {
    return OOTDPostPageResponse.builder()
        .hasNext(posts.hasNext())
        .posts(
            posts.getContent().stream()
                .map(
                    post ->
                        OOTDPostResponse.builder()
                            .id(post.getId())
                            .thumbnailImgUrl(post.getPostImage().getThumbnailImgUrl())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
