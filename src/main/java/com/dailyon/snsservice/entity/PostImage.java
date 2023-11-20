package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @OneToMany(
      mappedBy = "postImage",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @Builder.Default
  private List<PostImageProductDetail> postImageProductDetails = new ArrayList<>();

  @Column(name = "thumbnail_img_url", nullable = false)
  private String thumbnailImgUrl;

  @Column(name = "img_url", nullable = false)
  private String imgUrl;

  public static PostImage createPostImage(
      String thumbnailImgUrl, String imgUrl, List<PostImageProductDetail> postImageProductDetails) {
    PostImage postImage =
        PostImage.builder()
            .thumbnailImgUrl(thumbnailImgUrl)
            .imgUrl(imgUrl)
            .postImageProductDetails(postImageProductDetails)
            .build();
    postImageProductDetails.forEach(pipd -> pipd.setPostImage(postImage));
    return postImage;
  }

  public void setPost(Post post) {
    this.post = post;
  }
}
