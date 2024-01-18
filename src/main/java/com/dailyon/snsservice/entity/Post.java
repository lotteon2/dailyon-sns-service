package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.dto.request.post.UpdatePostRequest;
import com.dailyon.snsservice.entity.common.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(
      mappedBy = "post",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @Builder.Default
  private List<PostLike> postLikes = new ArrayList<>();

  @OneToOne(
      mappedBy = "post",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private PostImage postImage;

  @OneToMany(
      mappedBy = "post",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST})
  @Builder.Default
  private List<HashTag> hashTags = new ArrayList<>();

  @Column(name = "title", nullable = false)
  private String title;

  @Size(max = 300)
  @Column(name = "description")
  private String description;

  @Column(name = "stature")
  private Double stature;

  @Column(name = "weight")
  private Double weight;

  @Column(name = "view_count", nullable = false, columnDefinition = "integer default 0")
  @Builder.Default
  private Integer viewCount = 0;

  @Column(name = "like_count", nullable = false, columnDefinition = "integer default 0")
  @Builder.Default
  private Integer likeCount = 0;

  @Column(name = "comment_count", nullable = false, columnDefinition = "integer default 0")
  @Builder.Default
  private Integer commentCount = 0;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
  @Builder.Default
  private Boolean isDeleted = false;

  public static Post createPost(
      Member member,
      String title,
      String description,
      Double stature,
      Double weight,
      PostImage postImage,
      List<HashTag> hashTags) {
    Post post =
        Post.builder()
            .member(member)
            .title(title)
            .description(description)
            .stature(stature)
            .weight(weight)
            .hashTags(hashTags)
            .postImage(postImage)
            .build();
    postImage.setPost(post);
    hashTags.forEach(ht -> ht.setPost(post));
    return post;
  }

  public void updatePostAndPostImageProductDetail(UpdatePostRequest updatePostRequest) {
    this.title = updatePostRequest.getTitle();
    this.description = updatePostRequest.getDescription();
    this.stature = updatePostRequest.getStature();
    this.weight = updatePostRequest.getWeight();
    updatePostRequest
        .getHashTags()
        .forEach(
            updateHashTagRequest ->
                this.hashTags.forEach(
                    hashTag -> {
                      if (updateHashTagRequest.getId().equals(hashTag.getId())) {
                        if (!updateHashTagRequest.getName().equals(hashTag.getName())) {
                          hashTag.setName(updateHashTagRequest.getName());
                        }
                      }
                    }));
    this.postImage
        .getPostImageProductDetails()
        .forEach(
            existingDetail -> {
              updatePostRequest
                  .getPostImageProductDetails()
                  .forEach(
                      updatedDetail -> {
                        if (existingDetail.getId().equals(updatedDetail.getId())) {
                          existingDetail.updatePostImageProductDetail(
                              updatedDetail.getProductId(),
                              updatedDetail.getProductSize(),
                              updatedDetail.getLeftGapPercent(),
                              updatedDetail.getTopGapPercent());
                        }
                      });
            });
  }

  public void addViewCount(Integer count) {
    this.viewCount += count;
  }

  public void addLikeCount(Integer count) {
    this.likeCount += count;
  }

  public void addCommentCount(Integer count) {
    this.commentCount += count;
  }

  public void setDeleted(Boolean deleted) {
    this.isDeleted = deleted;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }
}
