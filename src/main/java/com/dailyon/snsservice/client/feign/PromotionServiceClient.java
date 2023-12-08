package com.dailyon.snsservice.client.feign;

import com.dailyon.snsservice.client.dto.CouponForProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "promotionServiceClient", url = "${endpoint.promotion-service}")
public interface PromotionServiceClient {

  @CircuitBreaker(
      name = "promotionServiceClient@getCouponsForProduct",
      fallbackMethod = "getCouponsForProductFallback")
  @GetMapping("/clients/coupons/coupons-existence")
  ResponseEntity<List<CouponForProductResponse>> getCouponsForProduct(
      @RequestHeader(name = "memberId") Long memberId,
      @RequestParam(name = "productIds") List<Long> productIds);

  default ResponseEntity<List<CouponForProductResponse>> getCouponsForProductFallback(
      @RequestHeader(name = "memberId") Long memberId,
      @RequestParam(name = "productIds") List<Long> productIds,
      Throwable t) {
    return ResponseEntity.ok(
        productIds.stream()
            .map(productId -> CouponForProductResponse.builder().productId(productId).build())
            .collect(Collectors.toList()));
  }
}
