package com.dailyon.snsservice.dto.response.post;

import com.dailyon.snsservice.dto.response.member.PostDetailMemberResponse;
import com.dailyon.snsservice.dto.response.postimageproductdetail.PostImageProductDetailResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
  private LocalDateTime createdAt;
  private PostDetailMemberResponse member;
  private List<PostDetailHashTagResponse> hashTags;
  private List<PostImageProductDetailResponse> postImageProductDetails;

  public PostDetailResponse(
      Long id,
      String title,
      String description,
      Double stature,
      Double weight,
      String imgUrl,
      LocalDateTime createdAt,
      PostDetailMemberResponse member,
      List<PostDetailHashTagResponse> hashTags,
      List<PostImageProductDetailResponse> postImageProductDetails) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.stature = stature;
    this.weight = weight;
    this.imgUrl = imgUrl;
    this.createdAt = createdAt;
    this.member = member;
    this.hashTags = hashTags;
    this.postImageProductDetails = postImageProductDetails;
  }
}
