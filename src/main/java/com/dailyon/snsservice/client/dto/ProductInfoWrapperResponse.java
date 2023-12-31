package com.dailyon.snsservice.client.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInfoWrapperResponse {

    private List<ProductInfoResponse> productInfos;
}
