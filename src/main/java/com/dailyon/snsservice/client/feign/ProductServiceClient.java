package com.dailyon.snsservice.client.feign;

import com.dailyon.snsservice.client.dto.ProductInfoResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "productServiceClient", url = "${endpoint.product-service}")
public interface ProductServiceClient {

  @GetMapping("/clients/products/post-image")
  ResponseEntity<List<ProductInfoResponse>> getProductInfos(
      @RequestParam(name = "id") List<Long> productIds);
}
