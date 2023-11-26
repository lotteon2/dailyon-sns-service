package com.dailyon.snsservice.client.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonRootName("productInfos")
public class ProductInfoResponse {

  private Long id;
  private String name;
  private String brandName;
  private String imgUrl;
  private Integer price;
}
