package com.dailyon.snsservice.client.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponForProductResponse {

  private Long productId;
  private Boolean hasAvailableCoupon;
}
