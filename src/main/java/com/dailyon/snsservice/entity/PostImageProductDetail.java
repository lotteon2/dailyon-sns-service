package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageProductDetail extends BaseEntity {

  @Id
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "post_image_id")
  private PostImage postImage;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "product_size", nullable = false)
  private String productSize;

  @Column(name = "left_gap_percent", nullable = false)
  private String leftGapPercent;

  @Column(name = "top_gap_percent", nullable = false)
  private String topGapPercent;
}
