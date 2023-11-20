package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.common.BaseEntity;
import com.dailyon.snsservice.entity.ids.HashTagId;
import javax.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(name = "name")
  private String name;

  public static HashTag createHashTag(String name) {
    return HashTag.builder().name(name).build();
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public void setName(String name) {
    this.name = name;
  }
}
