package com.dailyon.snsservice.dto.response.post;

import com.dailyon.snsservice.dto.response.member.PostDetailMemberResponse;
import com.dailyon.snsservice.dto.response.postimageproductdetail.PostImageProductDetailResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PostDetailResponse {

  private Long id;
  private String title;
  private String description;
  private Double stature;
  private Double weight;
  private String imgUrl;
  private Integer viewCount;
  private Integer likeCount;
  private Integer commentCount;

  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private Boolean isLike;

  private LocalDateTime createdAt;
  private PostDetailMemberResponse member;
  private Set<PostDetailHashTagResponse> hashTags = new HashSet<>();
  private Set<PostImageProductDetailResponse> postImageProductDetails = new HashSet<>();

  @QueryProjection
  public PostDetailResponse(
      Long id,
      String title,
      String description,
      Double stature,
      Double weight,
      String imgUrl,
      Integer viewCount,
      Integer likeCount,
      Integer commentCount,
      Boolean isLike,
      LocalDateTime createdAt,
      PostDetailMemberResponse member,
      Set<PostDetailHashTagResponse> hashTags,
      Set<PostImageProductDetailResponse> postImageProductDetails) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.stature = stature;
    this.weight = weight;
    this.imgUrl = imgUrl;
    this.viewCount = viewCount;
    this.likeCount = likeCount;
    this.commentCount = commentCount;
    this.isLike = isLike;
    this.createdAt = createdAt;
    this.member = member;
    this.hashTags = hashTags;
    this.postImageProductDetails = postImageProductDetails;
  }

  @QueryProjection
  public PostDetailResponse(
          Long id,
          String title,
          String description,
          Double stature,
          Double weight,
          String imgUrl,
          Integer viewCount,
          Integer likeCount,
          Integer commentCount,
          LocalDateTime createdAt,
          PostDetailMemberResponse member,
          Set<PostDetailHashTagResponse> hashTags,
          Set<PostImageProductDetailResponse> postImageProductDetails) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.stature = stature;
    this.weight = weight;
    this.imgUrl = imgUrl;
    this.viewCount = viewCount;
    this.likeCount = likeCount;
    this.commentCount = commentCount;
    this.createdAt = createdAt;
    this.member = member;
    this.hashTags = hashTags;
    this.postImageProductDetails = postImageProductDetails;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }
}
