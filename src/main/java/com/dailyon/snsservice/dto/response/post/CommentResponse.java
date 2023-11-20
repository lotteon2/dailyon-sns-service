package com.dailyon.snsservice.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

  private Long id;
  private String description;
  private LocalDateTime createdAt;
  private MemberResponse member;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<CommentResponse> replyComments;
}
