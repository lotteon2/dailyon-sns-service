package com.dailyon.snsservice.dto.request.post;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreatePostImageProductDetailRequest {

    @NotNull(message = "태그된 상품의 실제 id를 입력해주세요.")
    private Long productId;

    @NotBlank(message = "태그된 상품 치수를 등록해주세요.")
    private String productSize;

    @NotNull(message = "태그된 상품의 위치를 등록해주세요.")
    private Double leftGapPercent;

    @NotNull(message = "태그된 상품의 위치를 등록해주세요.")
    private Double topGapPercent;
}
