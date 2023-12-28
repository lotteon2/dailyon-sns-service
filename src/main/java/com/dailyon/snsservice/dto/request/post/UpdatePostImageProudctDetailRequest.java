package com.dailyon.snsservice.dto.request.post;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostImageProudctDetailRequest {

    @NotNull(message = "태그된 상품 id를 등록해주세요.")
    private Long id;

    @NotNull(message = "태그된 상품의 실제 id를 입력해주세요.")
    private Long productId;

    @NotBlank(message = "태그된 상품 치수를 등록해주세요.")
    private String productSize;

    @NotNull(message = "태그된 상품의 위치를 등록해주세요.")
    private Double leftGapPercent;

    @NotNull(message = "태그된 상품의 위치를 등록해주세요.")
    private Double topGapPercent;
}
