package com.dailyon.snsservice.dto.response.postimageproductdetail;

import com.dailyon.snsservice.client.dto.ProductInfoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PostImageProductDetailResponse {

  private Long id;
  private Long productId;
  private String name;
  private String brandName;
  private Integer price;
  private String imgUrl;
  private String size;
  private Double leftGapPercent;
  private Double topGapPercent;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean hasAvailableCoupon;

  @QueryProjection
  public PostImageProductDetailResponse(
      Long id, Long productId, String size, Double leftGapPercent, Double topGapPercent) {
    this.id = id;
    this.productId = productId;
    this.size = size;
    this.leftGapPercent = leftGapPercent;
    this.topGapPercent = topGapPercent;
  }

  public void setFromProductInfoResponse(ProductInfoResponse productInfoResponse) {
    this.name = productInfoResponse.getName();
    this.brandName = productInfoResponse.getBrandName();
    this.imgUrl = productInfoResponse.getImgUrl();
    this.price = productInfoResponse.getPrice();
  }

  public void setHasAvailableCoupon(Boolean hasAvailableCoupon) {
    this.hasAvailableCoupon = hasAvailableCoupon;
  }
}
