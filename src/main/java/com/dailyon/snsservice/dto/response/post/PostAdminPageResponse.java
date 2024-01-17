package com.dailyon.snsservice.dto.response.post;

import com.dailyon.snsservice.entity.HashTag;
import com.dailyon.snsservice.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAdminPageResponse {

  private int totalPages;
  private long totalElements;
  private List<PostAdminResponse> posts;

  public static PostAdminPageResponse fromEntity(Page<Post> posts) {
    return PostAdminPageResponse.builder()
        .totalPages(posts.getTotalPages())
        .totalElements(posts.getTotalElements())
        .posts(
            posts.getContent().stream()
                .map(
                    post ->
                        PostAdminResponse.builder()
                            .id(post.getId())
                            .thumbnailImgUrl(post.getPostImage().getThumbnailImgUrl())
                            .hashTagNames(
                                post.getHashTags().stream()
                                    .map(HashTag::getName)
                                    .collect(Collectors.toList()))
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .memberNickname(post.getMember().getNickname())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
