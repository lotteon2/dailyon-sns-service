package com.dailyon.snsservice.dto.response.post;

import com.dailyon.snsservice.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostPageResponse {

  private Boolean hasNext;
  private List<PostResponse> posts;

  public static PostPageResponse fromEntity(Long memberId, Page<Post> posts) {
    return PostPageResponse.builder()
        .hasNext(posts.hasNext())
        .posts(
            posts.getContent().stream()
                .map(
                    p ->
                        PostResponse.builder()
                            .id(p.getId())
                            .thumbnailImgUrl(p.getPostImage().getThumbnailImgUrl())
                            .viewCount(p.getViewCount())
                            .likeCount(p.getLikeCount())
                            .isLike(memberId != null ? !p.getPostLikes().isEmpty() : null)
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
