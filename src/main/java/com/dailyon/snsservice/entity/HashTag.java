package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.common.BaseEntity;
import com.dailyon.snsservice.entity.ids.HashTagId;
import javax.persistence.*;
import lombok.*;

@Getter
@Entity
@IdClass(HashTagId.class)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag extends BaseEntity {

  @Id
  @Column(name = "name")
  private String name;

  @Id
  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;
}
