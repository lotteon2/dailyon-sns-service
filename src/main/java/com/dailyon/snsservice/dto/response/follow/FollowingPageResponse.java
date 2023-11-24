package com.dailyon.snsservice.dto.response.follow;

import com.dailyon.snsservice.entity.Follow;
import com.dailyon.snsservice.entity.Member;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowingPageResponse {

  private int totalPages;
  private long totalElements;
  private List<FollowingResponse> followings;

  public static FollowingPageResponse fromEntity(Page<Follow> follows) {
    return FollowingPageResponse.builder()
        .totalPages(follows.getTotalPages())
        .totalElements(follows.getTotalElements())
        .followings(
            follows.getContent().stream()
                .map(
                    follow ->
                        FollowingResponse.builder()
                            .id(follow.getFollowing().getId())
                            .nickname(follow.getFollowing().getNickname())
                            .profileImgUrl(follow.getFollowing().getProfileImgUrl())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
