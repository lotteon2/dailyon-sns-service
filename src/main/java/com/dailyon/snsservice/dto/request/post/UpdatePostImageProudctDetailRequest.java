package com.dailyon.snsservice.dto.request.post;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UpdatePostImageProudctDetailRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long productId;

    @NotBlank(message = "태그된 상품 치수를 등록해주세요.")
    private String productSize;

    @NotNull
    private Double leftGapPercent;

    @NotNull
    private Double topGapPercent;
}
