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
