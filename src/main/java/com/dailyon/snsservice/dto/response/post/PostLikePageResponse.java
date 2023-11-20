package com.dailyon.snsservice.dto.response.post;

import java.util.List;
import java.util.stream.Collectors;

import com.dailyon.snsservice.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikePageResponse {

  private Boolean hasNext;
  private List<PostLikeResponse> posts;

  public static PostLikePageResponse fromEntity(Page<Post> posts) {
    return PostLikePageResponse.builder()
        .hasNext(posts.hasNext())
        .posts(
            posts.getContent().stream()
                .map(
                    p ->
                        PostLikeResponse.builder()
                            .id(p.getId())
                            .thumbnailImgUrl(p.getPostImage().getThumbnailImgUrl())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
