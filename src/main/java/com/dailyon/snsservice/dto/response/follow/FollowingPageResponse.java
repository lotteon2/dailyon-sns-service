package com.dailyon.snsservice.dto.response.follow;

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

  public static FollowingPageResponse fromEntity(Page<Member> followings) {
    return FollowingPageResponse.builder()
        .totalPages(followings.getTotalPages())
        .totalElements(followings.getTotalElements())
        .followings(
            followings.getContent().stream()
                .map(
                    following ->
                        FollowingResponse.builder()
                            .id(following.getId())
                            .nickname(following.getNickname())
                            .profileImgUrl(following.getProfileImgUrl())
                            .build())
                .collect(Collectors.toList()))
        .build();
  }
}
