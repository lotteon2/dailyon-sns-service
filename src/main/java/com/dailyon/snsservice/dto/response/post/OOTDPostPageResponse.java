package com.dailyon.snsservice.dto.response.post;

import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OOTDPostPageResponse {

  private int totalPages;
  private long totalElements;
  private List<OOTDPostResponse> posts;

  public static OOTDPostPageResponse fromDto(Page<OOTDPostResponse> myOOTDPostResponses) {
    return OOTDPostPageResponse.builder()
        .totalPages(myOOTDPostResponses.getTotalPages())
        .totalElements(myOOTDPostResponses.getTotalElements())
        .posts(
            myOOTDPostResponses.getContent().stream()
                .map(
                    post ->
                        OOTDPostResponse.builder()
                            .id(post.getId())
                            .thumbnailImgUrl(post.getThumbnailImgUrl())
                            .viewCount(post.getViewCount())
                            .likeCount(post.getLikeCount())
                            .isLike(post.getIsLike())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
