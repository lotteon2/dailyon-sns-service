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
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(
      mappedBy = "post",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @Builder.Default
  private List<PostLike> postLikes = new ArrayList<>();

  @OneToOne(
      mappedBy = "post",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private PostImage postImage;

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
  @Builder.Default
  private List<HashTag> hashTags = new ArrayList<>();

  @Column(name = "title", nullable = false)
  private String title;

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
            .postImage(postImage)
            .build();
    postImage.setPost(post);
    hashTags.forEach(ht -> ht.setPost(post));
    return post;
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
}
