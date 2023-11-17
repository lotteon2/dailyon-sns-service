package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

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

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "stature")
  private Double stature;

  @Column(name = "weight")
  private Double weight;

  @Column(name = "view_count", nullable = false)
  @Builder.Default
  private Integer viewCount = 0;

  @Column(name = "like_count", nullable = false)
  @Builder.Default
  private Integer likeCount = 0;

  @Column(name = "comment_count", nullable = false)
  @Builder.Default
  private Integer commentCount = 0;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  public static Post createPost(
      Member member, String title, String description, Double stature, Double weight) {
    return Post.builder()
        .member(member)
        .title(title)
        .description(description)
        .stature(stature)
        .weight(weight)
        .build();
  }
}
