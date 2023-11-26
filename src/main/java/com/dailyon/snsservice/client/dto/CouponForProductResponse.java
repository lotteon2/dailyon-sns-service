package com.dailyon.snsservice.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponForProductResponse {

  private Long productId;
  private String productName;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private CouponResponse coupon;
}
