package com.dailyon.snsservice.client.feign;

import com.dailyon.snsservice.client.dto.ProductInfoResponse;
import com.dailyon.snsservice.client.dto.ProductInfoWrapperResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productServiceClient", url = "${endpoint.product-service}")
public interface ProductServiceClient {

  @CircuitBreaker(
      name = "productServiceClient@getProductInfos",
      fallbackMethod = "getProductInfosFallback")
  @GetMapping("/clients/products/post-image")
  ResponseEntity<ProductInfoWrapperResponse> getProductInfos(
      @RequestParam(name = "id") List<Long> productIds);

  default ResponseEntity<ProductInfoWrapperResponse> getProductInfosFallback(
      @RequestParam(name = "id") List<Long> productIds, Throwable t) {
    return ResponseEntity.ok(
        ProductInfoWrapperResponse.builder()
            .productInfos(
                productIds.stream()
                    .map(productId -> ProductInfoResponse.builder().id(productId).build())
                    .collect(Collectors.toList()))
            .build());
  }
}
