package com.dailyon.snsservice.dto.response.follow;

import java.util.List;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowerPageResponse {

  private int totalPages;
  private long totalElements;
  private List<FollowerResponse> followers;

  public static FollowerPageResponse fromDto(Page<FollowerResponse> followerResponses) {
    return FollowerPageResponse.builder()
            .totalPages(followerResponses.getTotalPages())
            .totalElements(followerResponses.getTotalElements())
            .followers(followerResponses.getContent())
            .build();
  }
}
