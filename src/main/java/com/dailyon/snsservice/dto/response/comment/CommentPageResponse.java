package com.dailyon.snsservice.dto.response.comment;

import com.dailyon.snsservice.dto.response.member.MemberResponse;
import com.dailyon.snsservice.entity.Comment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentPageResponse {

  private int totalPages;
  private long totalElements;
  private List<CommentResponse> comments;

  public static CommentPageResponse fromEntity(Page<Comment> comments) {
    return CommentPageResponse.builder()
        .totalPages(comments.getTotalPages())
        .totalElements(comments.getTotalElements())
        .comments(
            comments.getContent().stream()
                .map(
                    comment ->
                        CommentResponse.builder()
                            .id(comment.getId())
                            .description(comment.getDescription())
                            .isDeleted(comment.getIsDeleted())
                            .createdAt(createFormattedDate(comment.getCreatedAt()))
                            .member(
                                MemberResponse.builder()
                                    .id(comment.getMember().getId())
                                    .nickname(comment.getMember().getNickname())
                                    .profileImgUrl(comment.getMember().getProfileImgUrl())
                                    .build())
                            .replyComments(
                                comment.getChildren().stream()
                                    .map(
                                        replyComment ->
                                            CommentResponse.builder()
                                                .id(replyComment.getId())
                                                .description(replyComment.getDescription())
                                                .isDeleted(replyComment.getIsDeleted())
                                                .createdAt(
                                                    createFormattedDate(
                                                        replyComment.getCreatedAt()))
                                                .member(
                                                    MemberResponse.builder()
                                                        .id(replyComment.getMember().getId())
                                                        .nickname(
                                                            replyComment.getMember().getNickname())
                                                        .profileImgUrl(
                                                            replyComment
                                                                .getMember()
                                                                .getProfileImgUrl())
                                                        .build())
                                                .build())
                                    .collect(Collectors.toList()))
                            .build())
                .collect(Collectors.toList()))
        .build();
  }

  private static String createFormattedDate(LocalDateTime localDateTime) {
    String formattedCreatedAt = "";
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(localDateTime, now);
    if (duration.toMinutes() < 1) {
      formattedCreatedAt = "방금";
    } else if (duration.toMinutes() < 60) {
      formattedCreatedAt = String.format("%s분전", duration.toMinutes());
    } else if (duration.toHours() < 24) {
      formattedCreatedAt = String.format("%s시간전", duration.toHours());
    } else if (duration.toDays() < 7) {
      formattedCreatedAt = String.format("%s일전", duration.toDays());
    } else if (duration.toDays() < 31) {
      formattedCreatedAt = String.format("%s주전", duration.toDays() / 7);
    } else if (duration.toDays() < 365) {
      formattedCreatedAt = String.format("%s개월전", duration.toDays() / 31);
    } else {
      formattedCreatedAt = String.format("%s년전", duration.toDays() / 365);
    }

    return formattedCreatedAt;
  }
}
