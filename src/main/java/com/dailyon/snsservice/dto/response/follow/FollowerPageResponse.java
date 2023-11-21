package com.dailyon.snsservice.dto.response.follow;

import com.dailyon.snsservice.entity.Member;
import java.util.List;
import java.util.stream.Collectors;
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

  public static FollowerPageResponse fromEntity(Page<Member> followers) {
    return FollowerPageResponse.builder()
        .totalPages(followers.getTotalPages())
        .totalElements(followers.getTotalElements())
        .followers(followers.getContent().stream()
                .map(
                        follower ->
                                FollowerResponse.builder()
                                        .id(follower.getId())
                                        .nickname(follower.getNickname())
                                        .profileImgUrl(follower.getProfileImgUrl())
                                        .build())
                .collect(Collectors.toList()))
        .build();
  }
}
