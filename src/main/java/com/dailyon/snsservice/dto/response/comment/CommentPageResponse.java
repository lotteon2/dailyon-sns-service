package com.dailyon.snsservice.dto.response.comment;

import com.dailyon.snsservice.dto.response.member.MemberResponse;
import com.dailyon.snsservice.entity.Comment;
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
                            .createdAt(comment.getCreatedAt())
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
                                                .createdAt(replyComment.getCreatedAt())
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
}
