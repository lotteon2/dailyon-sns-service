package com.dailyon.snsservice.client.feign;

import com.dailyon.snsservice.client.dto.CouponForProductResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "promotionServiceClient", url = "${endpoint.promotion-service}")
public interface PromotionServiceClient {

  @GetMapping("/clients/coupons")
  ResponseEntity<List<CouponForProductResponse>> getCouponsForProduct(
      @RequestHeader(name = "memberId") Long memberId,
      @RequestParam(name = "type") String couponType,
      @RequestParam(name = "productIds") List<Long> productIds);
}
