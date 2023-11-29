package com.dailyon.snsservice.dto.response.postlike;

import com.dailyon.snsservice.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikePageResponse {

  private int totalPages;
  private long totalElements;
  private List<PostLikeResponse> posts;

  public static PostLikePageResponse fromEntity(Page<Post> posts) {
    return PostLikePageResponse.builder()
        .totalPages(posts.getTotalPages())
        .totalElements(posts.getTotalElements())
        .posts(
            posts.getContent().stream()
                .map(
                    p ->
                        PostLikeResponse.builder()
                            .id(p.getId())
                            .thumbnailImgUrl(p.getPostImage().getThumbnailImgUrl())
                            .viewCount(p.getViewCount())
                            .likeCount(p.getLikeCount())
                            .isLike(true)
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
