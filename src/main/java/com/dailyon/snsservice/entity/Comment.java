package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @Builder.Default
  @BatchSize(size = 100)
  private List<Comment> children = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Size(min = 5, max = 140)
  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
  @Builder.Default
  private Boolean isDeleted = false;

  public static Comment createComment(Member member, Post post, String description) {
    return Comment.builder()
        .member(member)
        .post(post)
        .description(description)
        .build();
  }

  public static Comment createReplyComment(
          Comment parent, Member member, Post post, String description) {
    return Comment.builder()
            .parent(parent)
            .member(member)
            .post(post)
            .description(description)
            .build();
  }

  public void setDeleted(Boolean deleted) {
    isDeleted = deleted;
  }
}
