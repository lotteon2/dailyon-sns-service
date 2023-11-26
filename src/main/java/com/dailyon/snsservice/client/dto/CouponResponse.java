package com.dailyon.snsservice.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponResponse {

  private String couponName;
  private String type;

  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private Long discountRate;

  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private Long discountAmount;
}
